package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.Album;
import cn.edu.hbnu.sonic.mapper.AlbumMapper;
import cn.edu.hbnu.sonic.service.AlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {
}