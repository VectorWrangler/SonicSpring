package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.common.MediaUrlUtils;
import cn.edu.hbnu.sonic.dto.SongDTO;
import cn.edu.hbnu.sonic.entity.*;
import cn.edu.hbnu.sonic.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    private final SongService songService;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final PlaylistService playlistService;
    private final MvService mvService;

    public SearchServiceImpl(SongService songService, AlbumService albumService, 
                           ArtistService artistService, PlaylistService playlistService, 
                           MvService mvService) {
        this.songService = songService;
        this.albumService = albumService;
        this.artistService = artistService;
        this.playlistService = playlistService;
        this.mvService = mvService;
    }

    @Override
    public Map<String, Object> search(String keywords, Integer limit, Integer offset, Integer type) {

        // 根据搜索类型执行不同的搜索逻辑

        return switch (type) {
            case 1 -> // 单曲
                    {
                        List<SongDTO> songs = this.searchSongs(keywords, limit, offset);
                        Map<String, Object> result = new HashMap<>();
                        result.put("songs", songs);
                        result.put("totalCount", songs.size());
                        yield result;
                    }
            case 10 -> // 专辑
                    searchAlbums(keywords, limit, offset);
            case 100 -> // 歌手
                    searchArtists(keywords, limit, offset);
            case 1000 -> // 歌单
                    searchPlaylists(keywords, limit, offset);
            case 1002 -> // MV
                    searchMvs(keywords, limit, offset);
            default -> // 默认搜索单曲
                    {
                        List<SongDTO> songs = this.searchSongs(keywords, limit, offset);
                        Map<String, Object> result = new HashMap<>();
                        result.put("songs", songs);
                        result.put("totalCount", songs.size());
                        yield result;
                    }
        };
    }
    
    @Override
    public List<SongDTO> searchSongs(String keywords, Integer limit, Integer offset) {
        // 构建查询条件
        List<Song> songs = songService.lambdaQuery()
                .like(Song::getName, keywords)
                .or()
                .like(Song::getLyric, keywords)
                .last("LIMIT " + limit + " OFFSET " + offset)
                .list();
        
        // 转换为SongDTO列表
        return songService.convertToDTOs(songs);
    }
    
    /**
     * 搜索专辑
     */
    private Map<String, Object> searchAlbums(String keywords, Integer limit, Integer offset) {
        // 构建查询条件
        List<Album> albums = albumService.lambdaQuery()
                .like(Album::getName, keywords)
                .last("LIMIT " + limit + " OFFSET " + offset)
                .list();
        
        // 处理专辑封面URL
        albums.forEach(album -> {
            if (album.getCover() != null && !album.getCover().startsWith("http")) {
                album.setCover(MediaUrlUtils.getInstance().processLyric(album.getCover()));
            }
        });
        
        // 获取总数
        long totalCount = albumService.lambdaQuery()
                .like(Album::getName, keywords)
                .count();
        
        Map<String, Object> result = new HashMap<>();
        result.put("albums", albums);
        result.put("totalCount", totalCount);
        return result;
    }
    
    /**
     * 搜索歌手
     */
    private Map<String, Object> searchArtists(String keywords, Integer limit, Integer offset) {
        // 构建查询条件
        List<Artist> artists = artistService.lambdaQuery()
                .like(Artist::getName, keywords)
                .last("LIMIT " + limit + " OFFSET " + offset)
                .list();
        
        // 处理歌手头像URL
        artists.forEach(artist -> {
            if (artist.getAvatar() != null && !artist.getAvatar().startsWith("http")) {
                artist.setAvatar(MediaUrlUtils.getInstance().processLyric(artist.getAvatar()));
            }
        });
        
        // 获取总数
        long totalCount = artistService.lambdaQuery()
                .like(Artist::getName, keywords)
                .count();
        
        Map<String, Object> result = new HashMap<>();
        result.put("artists", artists);
        result.put("totalCount", totalCount);
        return result;
    }
    
    /**
     * 搜索歌单
     */
    private Map<String, Object> searchPlaylists(String keywords, Integer limit, Integer offset) {
        // 构建查询条件
        List<Playlist> playlists = playlistService.lambdaQuery()
                .like(Playlist::getName, keywords)
                .or()
                .like(Playlist::getDescription, keywords)
                .last("LIMIT " + limit + " OFFSET " + offset)
                .list();
        
        // 处理歌单封面URL
        playlists.forEach(playlist -> {
            if (playlist.getCover() != null && !playlist.getCover().startsWith("http")) {
                playlist.setCover(MediaUrlUtils.getInstance().processLyric(playlist.getCover()));
            }
        });
        
        // 获取总数
        long totalCount = playlistService.lambdaQuery()
                .like(Playlist::getName, keywords)
                .or()
                .like(Playlist::getDescription, keywords)
                .count();
        
        Map<String, Object> result = new HashMap<>();
        result.put("playlists", playlists);
        result.put("totalCount", totalCount);
        return result;
    }
    
    /**
     * 搜索MV
     */
    private Map<String, Object> searchMvs(String keywords, Integer limit, Integer offset) {
        // 构建查询条件
        List<Mv> mvs = mvService.lambdaQuery()
                .like(Mv::getTitle, keywords)
                .or()
                .like(Mv::getArtist, keywords)
                .last("LIMIT " + limit + " OFFSET " + offset)
                .list();
        
        // 处理MV URL
        List<Mv> processedMvs = MediaUrlUtils.getInstance().processMvListUrls(mvs);
        
        // 获取总数
        long totalCount = mvService.lambdaQuery()
                .like(Mv::getTitle, keywords)
                .or()
                .like(Mv::getArtist, keywords)
                .count();
        
        Map<String, Object> result = new HashMap<>();
        result.put("mvs", processedMvs);
        result.put("totalCount", totalCount);
        return result;
    }
}