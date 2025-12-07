package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.SongArtist;
import cn.edu.hbnu.sonic.mapper.SongArtistMapper;
import cn.edu.hbnu.sonic.service.SongArtistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SongArtistServiceImpl extends ServiceImpl<SongArtistMapper, SongArtist> implements SongArtistService {
}