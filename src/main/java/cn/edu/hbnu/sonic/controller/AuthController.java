package cn.edu.hbnu.sonic.controller;

import cn.edu.hbnu.sonic.common.Result;
import cn.edu.hbnu.sonic.dto.LoginRequestDTO;
import cn.edu.hbnu.sonic.entity.User;
import cn.edu.hbnu.sonic.service.UserService;
import cn.edu.hbnu.sonic.service.FileUploadService;
import cn.edu.hbnu.sonic.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Tag(name = "认证模块", description = "用户认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, 
                          FileUploadService fileUploadService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.fileUploadService = fileUploadService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "用户登录", description = "用户登录接口，成功后返回JWT token和用户基本信息")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Parameter(description = "登录请求参数") @RequestBody LoginRequestDTO loginRequest) {
        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 调用userService处理登录成功后的逻辑
            Map<String, Object> result = userService.login(loginRequest);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("登录失败: ", e);
            return Result.error("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result<Map<String, String>> register(@RequestParam("username") String username,
                                               @RequestParam("password") String password,
                                               @RequestParam(value = "email", required = false) String email,
                                               @RequestParam(value = "phone", required = false) String phone,
                                               @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            // 对密码进行加密
            String encodedPassword = passwordEncoder.encode(password);
            Map<String, String> result = userService.register(username, encodedPassword, email, phone, avatar);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("注册失败: ", e);
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "用户登出", description = "用户登出接口，清除用户认证信息")
    @PostMapping("/logout")
    public Result<Map<String, String>> logout() {
        try {
            // 清除安全上下文
            SecurityContextHolder.clearContext();
            
            Map<String, String> result = userService.logout();
            return Result.success(result);
        } catch (Exception e) {
            logger.error("登出失败: ", e);
            return Result.error("登出失败");
        }
    }
}