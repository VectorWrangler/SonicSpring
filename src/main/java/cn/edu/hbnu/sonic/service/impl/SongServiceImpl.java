package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.dto.IdNameDTO;
import cn.edu.hbnu.sonic.dto.SongDTO;
import cn.edu.hbnu.sonic.entity.Album;
import cn.edu.hbnu.sonic.entity.Artist;
import cn.edu.hbnu.sonic.entity.Song;
import cn.edu.hbnu.sonic.entity.SongArtist;
import cn.edu.hbnu.sonic.mapper.SongMapper;
import cn.edu.hbnu.sonic.service.AlbumService;
import cn.edu.hbnu.sonic.service.ArtistService;
import cn.edu.hbnu.sonic.service.SongArtistService;
import cn.edu.hbnu.sonic.service.SongService;
import cn.edu.hbnu.sonic.common.MediaUrlUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {
    
    @Autowired
    private ArtistService artistService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private SongArtistService songArtistService;
    
    @Override
    public SongDTO convertToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setId(song.getId());
        dto.setName(song.getName()); // 使用数据库查出来的name
        dto.setDuration(song.getDuration());
        dto.setUrl(MediaUrlUtils.getInstance().processLyric(song.getUrl()));
        dto.setLyric(song.getLyric());
        dto.setCreateTime(song.getCreateTime());
        dto.setUpdateTime(song.getUpdateTime());
        
        // 设置歌手列表信息
        List<IdNameDTO> artists = new ArrayList<>();
        // 查询歌曲和歌手的关联关系
        List<SongArtist> songArtists = songArtistService.lambdaQuery()
                .eq(SongArtist::getSongId, song.getId())
                .list();
        
        if (!songArtists.isEmpty()) {
            // 获取所有歌手ID
            List<Long> artistIds = songArtists.stream()
                    .map(SongArtist::getArtistId)
                    .collect(Collectors.toList());
            
            // 批量查询歌手信息
            List<Artist> artistEntities = artistService.listByIds(artistIds);
            
            // 转换为IdNameDTO列表
            artists = artistEntities.stream()
                    .map(artist -> {
                        IdNameDTO idNameDTO = new IdNameDTO();
                        idNameDTO.setId(artist.getId());
                        idNameDTO.setName(artist.getName());
                        return idNameDTO;
                    })
                    .collect(Collectors.toList());
        }
        dto.setArtists(artists);
        
        // 设置专辑信息
        IdNameDTO album = new IdNameDTO();
        album.setId(song.getAlbumId());
        // 查询真实的专辑名称和封面
        if (song.getAlbumId() != null) {
            Album albumEntity = albumService.getById(song.getAlbumId());
            if (albumEntity != null) {
                album.setName(albumEntity.getName());
                // 从专辑中获取封面信息
                String coverUrl = albumEntity.getCover();
                if (coverUrl != null && !coverUrl.startsWith("http")) {
                    coverUrl = MediaUrlUtils.getInstance().processLyric(coverUrl);
                }
                dto.setCover(coverUrl);
            } else {
                album.setName("");
                dto.setCover(null);
            }
        } else {
            album.setName("");
            dto.setCover(null);
        }
        dto.setAlbum(album);
        
        return dto;
    }
    
    @Override
    public List<SongDTO> convertToDTOs(List<Song> songs) {
        return songs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SongDTO> getSongsWithPagination(Integer limit, Integer offset) {
        // 获取总歌曲数
        long totalSongs = this.count();
        
        // 如果没有指定limit，则返回所有歌曲
        int actualLimit = (limit != null) ? limit : (int) totalSongs;
        
        // 计算实际的起始位置和结束位置
        int startIndex = offset != null ? offset : 0;
        
        // 如果起始位置超出范围，返回空列表
        if (startIndex >= totalSongs) {
            return List.of();
        }
        
        // 使用LambdaQueryWrapper进行分页查询
        LambdaQueryWrapper<Song> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.last("LIMIT " + actualLimit + " OFFSET " + startIndex);
        
        List<Song> songs = this.list(queryWrapper);
        
        // 转换为SongDTO
        return this.convertToDTOs(songs);
    }
}