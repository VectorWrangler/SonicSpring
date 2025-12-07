package cn.edu.hbnu.sonic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("song_artist")
public class SongArtist {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("song_id")
    private Long songId;
    
    @TableField("artist_id")
    private Long artistId;
    
    @TableField("create_time")
    private LocalDateTime createTime;
}