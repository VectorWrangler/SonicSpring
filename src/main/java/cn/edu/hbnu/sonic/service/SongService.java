package cn.edu.hbnu.sonic.service;

import cn.edu.hbnu.sonic.dto.SongDTO;
import cn.edu.hbnu.sonic.entity.Song;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SongService extends IService<Song> {
    /**
     * 将Song实体转换为SongDTO
     * @param song Song实体
     * @return SongDTO
     */
    SongDTO convertToDTO(Song song);
    
    /**
     * 批量将Song实体转换为SongDTO
     * @param songs Song实体列表
     * @return SongDTO列表
     */
    List<SongDTO> convertToDTOs(List<Song> songs);
    
    /**
     * 分页获取歌曲列表
     * @param limit 限制数量
     * @param offset 偏移量
     * @return SongDTO列表
     */
    List<SongDTO> getSongsWithPagination(Integer limit, Integer offset);
}