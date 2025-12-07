package cn.edu.hbnu.sonic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("playlist")
public class Playlist {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String cover;
    
    private String description;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("play_count")
    private Long playCount;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}