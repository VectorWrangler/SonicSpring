package cn.edu.hbnu.sonic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("album")
public class Album {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String cover;
    
    @TableField("artist_id")
    private Long artistId;
    
    @TableField("release_date")
    private LocalDate releaseDate;
    
    private String description;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}