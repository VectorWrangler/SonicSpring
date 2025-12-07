package cn.edu.hbnu.sonic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("artist")
public class Artist {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String avatar;
    
    private String description;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}