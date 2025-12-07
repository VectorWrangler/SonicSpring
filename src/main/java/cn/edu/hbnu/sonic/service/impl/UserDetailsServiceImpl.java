package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.User;
import cn.edu.hbnu.sonic.entity.UserDetailsImpl;
import cn.edu.hbnu.sonic.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.lambdaQuery()
                .eq(User::getUsername, username)
                .one();

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        return new UserDetailsImpl(user);
    }
}