package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.Artist;
import cn.edu.hbnu.sonic.mapper.ArtistMapper;
import cn.edu.hbnu.sonic.service.ArtistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl extends ServiceImpl<ArtistMapper, Artist> implements ArtistService {
}