package cn.edu.hbnu.sonic.controller;

import cn.edu.hbnu.sonic.entity.Playlist;
import cn.edu.hbnu.sonic.entity.PlaylistSong;
import cn.edu.hbnu.sonic.entity.Song;
import cn.edu.hbnu.sonic.service.PlaylistService;
import cn.edu.hbnu.sonic.service.PlaylistSongService;
import cn.edu.hbnu.sonic.service.SongService;
import cn.edu.hbnu.sonic.common.MediaUrlUtils;
import cn.edu.hbnu.sonic.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "歌单模块", description = "歌单相关接口")
@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final SongService songService;
    private final PlaylistSongService playlistSongService;

    public PlaylistController(PlaylistService playlistService, SongService songService, PlaylistSongService playlistSongService) {
        this.playlistService = playlistService;
        this.songService = songService;
        this.playlistSongService = playlistSongService;
    }
    
    @Operation(summary = "获取所有歌单", description = "获取所有歌单列表")
    @GetMapping
    public Result<List<Playlist>> getAllPlaylists() {
        List<Playlist> playlists = playlistService.list();
        return Result.success(playlists);
    }
    
    @Operation(summary = "根据ID获取歌单", description = "根据歌单ID获取歌单详情")
    @GetMapping("/{id}")
    public Result<Playlist> getPlaylistById(
            @Parameter(description = "歌单ID") 
            @PathVariable Long id) {
        Playlist playlist = playlistService.getById(id);
        if (playlist == null) {
            return Result.notFound("歌单不存在");
        }
        return Result.success(playlist);
    }

    
    @Operation(summary = "获取歌单中的歌曲", description = "根据歌单ID获取其中的歌曲列表，支持分页")
    @GetMapping("/track/all")
    public Result<List<Song>> getPlaylistSongs(
            @Parameter(description = "歌单ID") 
            @RequestParam Long id,
            @Parameter(description = "返回数量") 
            @RequestParam(required = false) Integer limit,
            @Parameter(description = "偏移数量，用于分页，默认为0") 
            @RequestParam(defaultValue = "0") Integer offset) {
        Playlist playlist = playlistService.getById(id);
        if (playlist == null) {
            return Result.notFound("歌单不存在");
        }
        
        // 查询关联表获取歌曲ID列表
        List<PlaylistSong> playlistSongs = playlistSongService.lambdaQuery()
                .eq(PlaylistSong::getPlaylistId, id)
                .list();
        
        if (playlistSongs.isEmpty()) {
            return Result.success(Collections.emptyList());
        }
        
        // 提取歌曲ID
        List<Long> songIds = playlistSongs.stream()
                .map(PlaylistSong::getSongId)
                .collect(Collectors.toList());
        
        // 如果没有指定limit，则返回所有歌曲
        int actualLimit = (limit != null) ? limit : songIds.size();
        
        // 计算实际的起始位置和结束位置
        int startIndex = offset;
        int endIndex = Math.min(offset + actualLimit, songIds.size());
        
        // 如果起始位置超出范围，返回空列表
        if (startIndex >= songIds.size()) {
            return Result.success(Collections.emptyList());
        }
        
        // 截取指定范围的歌曲ID
        List<Long> paginatedSongIds = songIds.subList(startIndex, endIndex);
        
        // 获取歌曲列表
        List<Song> songs = songService.listByIds(paginatedSongIds);
        
        // 为每首歌曲的url和cover添加媒体服务器地址前缀
        List<Song> processedSongs = MediaUrlUtils.getInstance().processSongListUrls(songs);
        return Result.success(processedSongs);
    }
}