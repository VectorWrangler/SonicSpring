package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.User;
import cn.edu.hbnu.sonic.mapper.UserMapper;
import cn.edu.hbnu.sonic.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}