package cn.edu.hbnu.sonic.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SongDTO {
    private Long id;

    private String name;

    /**
     * 歌手列表
     */
    private List<IdNameDTO> artists;

    /**
     * 专辑
     */
    private IdNameDTO album;

    private Integer duration;

    private String url;

    private String lyric;

    /**
     * 歌曲对应专辑的封面图片
     */
    private String cover;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}