package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.PlaylistSong;
import cn.edu.hbnu.sonic.mapper.PlaylistSongMapper;
import cn.edu.hbnu.sonic.service.PlaylistSongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PlaylistSongServiceImpl extends ServiceImpl<PlaylistSongMapper, PlaylistSong> implements PlaylistSongService {
}