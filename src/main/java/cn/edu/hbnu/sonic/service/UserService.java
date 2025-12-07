package cn.edu.hbnu.sonic.service;

import cn.edu.hbnu.sonic.dto.LoginRequestDTO;
import cn.edu.hbnu.sonic.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     * @param loginRequest 登录请求参数
     * @return 登录结果，包含token和用户信息
     */
    Map<String, Object> login(LoginRequestDTO loginRequest);
    
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱（可选）
     * @param phone 手机号（可选）
     * @param avatar 头像文件（可选）
     * @return 注册结果
     */
    Map<String, String> register(String username, String password, String email, String phone, MultipartFile avatar);
    
    /**
     * 用户登出
     * @return 登出结果
     */
    Map<String, String> logout();
}