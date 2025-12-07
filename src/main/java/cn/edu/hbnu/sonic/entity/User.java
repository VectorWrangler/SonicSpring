package cn.edu.hbnu.sonic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("`user`")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String phone;
    
    private String avatar;
    
    private String role;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}