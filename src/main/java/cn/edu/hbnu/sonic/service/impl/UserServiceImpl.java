package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.common.MediaUrlUtils;
import cn.edu.hbnu.sonic.dto.LoginRequestDTO;
import cn.edu.hbnu.sonic.entity.User;
import cn.edu.hbnu.sonic.mapper.UserMapper;
import cn.edu.hbnu.sonic.service.UserService;
import cn.edu.hbnu.sonic.service.FileUploadService;
import cn.edu.hbnu.sonic.util.JwtUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private MediaUrlUtils mediaUrlUtils;
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @Override
    public Map<String, Object> login(LoginRequestDTO loginRequest) {
        // 注意：实际的认证逻辑应该在AuthController或Security层面处理
        // 这里仅处理登录成功后的逻辑
        
        try {
            // 生成JWT token
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            
            // 获取用户详细信息
            User user = this.lambdaQuery()
                    .eq(User::getUsername, loginRequest.getUsername())
                    .one();
            
            // 处理用户头像URL，添加媒体服务器地址前缀
            user = mediaUrlUtils.processUserAvatarUrl(user);
            
            // 构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("avatar", user.getAvatar());
            response.put("email", user.getEmail());
            response.put("message", "登录成功");
            
            return response;
        } catch (Exception e) {
            logger.error("登录失败: ", e);
            throw new RuntimeException("登录处理失败");
        }
    }
    
    @Override
    public Map<String, String> register(String username, String password, String email, String phone, MultipartFile avatar) {
        logger.info("开始注册用户: {}", username);
        logger.debug("请求参数 - username: {}, email: {}, phone: {}", username, email, phone);
        
        // 记录avatar参数的详细信息
        if (avatar == null) {
            logger.debug("avatar参数为null");
        } else {
            logger.debug("收到avatar文件 - 名称: {}, 大小: {} bytes, 类型: {}", 
                       avatar.getOriginalFilename(), avatar.getSize(), avatar.getContentType());
        }
        
        // 检查用户名是否已存在
        User existingUser = this.lambdaQuery()
                .eq(User::getUsername, username)
                .one();
                
        if (existingUser != null) {
            logger.info("用户名已存在: {}", username);
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在（如果提供了邮箱）
        if (email != null && !email.isEmpty()) {
            User existingEmailUser = this.lambdaQuery()
                    .eq(User::getEmail, email)
                    .one();
                    
            if (existingEmailUser != null) {
                logger.info("邮箱已存在: {}", email);
                throw new RuntimeException("邮箱已被使用");
            }
        }

        // 创建新用户
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // 注意：这里不再加密密码，因为应该在Controller层处理
        
        // 设置可选字段，只在不为空时设置
        if (email != null && !email.isEmpty()) {
            newUser.setEmail(email);
        }
        
        if (phone != null && !phone.isEmpty()) {
            newUser.setPhone(phone);
        }
        
        newUser.setRole("normal"); // 设置默认角色为normal
        
        try {
            // 先保存用户以获取用户ID
            logger.debug("保存用户到数据库...");
            this.save(newUser);
            logger.debug("用户保存成功，ID: {}", newUser.getId());
            
            // 处理头像上传
            if (avatar != null && !avatar.isEmpty()) {
                try {
                    logger.debug("开始上传头像文件...");
                    // 使用用户ID作为文件名上传头像
                    String avatarUrl = fileUploadService.uploadAvatar(avatar, newUser.getId());
                    logger.info("头像上传成功，URL: {}", avatarUrl);
                    // 更新用户的avatar字段
                    newUser.setAvatar(avatarUrl);
                    this.updateById(newUser);
                    logger.info("用户头像字段更新成功");
                } catch (Exception e) {
                    logger.error("头像上传失败: ", e);
                    // 如果头像上传失败，回滚用户创建操作
                    this.removeById(newUser.getId());
                    throw new RuntimeException("头像上传失败: " + e.getMessage());
                }
            } else {
                if (avatar == null) {
                    logger.info("没有提供头像文件");
                } else {
                    logger.info("提供的头像文件为空");
                }
            }
        } catch (DuplicateKeyException e) {
            logger.error("数据库约束违反: ", e);
            throw new RuntimeException("用户信息已存在");
        } catch (Exception e) {
            logger.error("注册过程中发生未知错误: ", e);
            throw new RuntimeException("注册失败: " + e.getMessage());
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "注册成功");
        return response;
    }
    
    @Override
    public Map<String, String> logout() {
        // 登出逻辑
        Map<String, String> response = new HashMap<>();
        response.put("message", "登出成功");
        return response;
    }
}