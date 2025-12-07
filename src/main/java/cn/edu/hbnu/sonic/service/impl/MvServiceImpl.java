package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.Mv;
import cn.edu.hbnu.sonic.mapper.MvMapper;
import cn.edu.hbnu.sonic.service.MvService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MvServiceImpl extends ServiceImpl<MvMapper, Mv> implements MvService {
}