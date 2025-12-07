package cn.edu.hbnu.sonic.controller;

import cn.edu.hbnu.sonic.dto.SongDTO;
import cn.edu.hbnu.sonic.entity.Song;
import cn.edu.hbnu.sonic.service.SongService;
import cn.edu.hbnu.sonic.common.MediaUrlUtils;
import cn.edu.hbnu.sonic.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "歌曲模块", description = "歌曲相关接口")
@RestController
@RequestMapping("/song")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @Operation(summary = "获取所有歌曲", description = "分页获取所有歌曲列表")
    @GetMapping("/allSongs")
    public Result<List<SongDTO>> getAllSongs(
            @Parameter(description = "返回数量") 
            @RequestParam(required = false) Integer limit,
            @Parameter(description = "偏移数量，用于分页，默认为0") 
            @RequestParam(defaultValue = "0") Integer offset) {
        List<SongDTO> songDTOs = songService.getSongsWithPagination(limit, offset);
        return Result.success(songDTOs);
    }
    
    @Operation(summary = "根据ID获取歌曲", description = "根据歌曲ID获取歌曲详情")
    @GetMapping("/url")
    public Result<Song> getSongById(
            @Parameter(description = "歌曲ID") 
            @RequestParam Long id) {
        Song song = songService.getById(id);
        if (song == null) {
            return Result.notFound("歌曲不存在");
        }
        // 为歌曲的url和cover添加媒体服务器地址前缀
        Song processedSong = MediaUrlUtils.getInstance().processSongUrls(song);
        return Result.success(processedSong);
    }
}