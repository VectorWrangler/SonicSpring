package cn.edu.hbnu.sonic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("song")
public class Song {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    @TableField("artist_id")
    private Long artistId;
    
    @TableField("album_id")
    private Long albumId;
    
    private Integer duration;
    
    private String url;
    
    private String lyric;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;

    // 其他字段

}