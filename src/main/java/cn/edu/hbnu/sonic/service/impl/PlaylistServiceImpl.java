package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.Playlist;
import cn.edu.hbnu.sonic.mapper.PlaylistMapper;
import cn.edu.hbnu.sonic.service.PlaylistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PlaylistServiceImpl extends ServiceImpl<PlaylistMapper, Playlist> implements PlaylistService {
}