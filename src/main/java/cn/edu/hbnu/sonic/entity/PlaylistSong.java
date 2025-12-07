package cn.edu.hbnu.sonic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("playlist_song")
public class PlaylistSong {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("playlist_id")
    private Long playlistId;
    
    @TableField("song_id")
    private Long songId;
    
    @TableField("create_time")
    private LocalDateTime createTime;
}