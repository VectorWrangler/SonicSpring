package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.Favorite;
import cn.edu.hbnu.sonic.mapper.FavoriteMapper;
import cn.edu.hbnu.sonic.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
}