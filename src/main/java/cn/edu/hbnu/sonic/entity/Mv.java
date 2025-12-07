package cn.edu.hbnu.sonic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("mv")
public class Mv {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String artist;
    
    @TableField("video_url")
    private String videoUrl;
    
    private String cover;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}