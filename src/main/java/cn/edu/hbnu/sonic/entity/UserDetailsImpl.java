package cn.edu.hbnu.sonic.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class UserDetailsImpl implements UserDetails {
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 根据用户的角色设置权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 所有用户都有USER角色
        authorities.add(new SimpleGrantedAuthority("USER"));
        
        // 根据用户的角色添加特定权限
        if (user.getRole() != null) {
            switch (user.getRole()) {
                case "admin":
                    authorities.add(new SimpleGrantedAuthority("ADMIN"));
                    break;
                case "vip":
                    authorities.add(new SimpleGrantedAuthority("VIP"));
                    break;
                case "svip":
                    authorities.add(new SimpleGrantedAuthority("SVIP"));
                    break;
                default:
                    // normal用户只拥有USER权限
                    break;
            }
        }
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}