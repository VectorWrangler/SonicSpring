package cn.edu.hbnu.sonic.config;

import cn.edu.hbnu.sonic.filter.JwtAuthenticationFilter;
import cn.edu.hbnu.sonic.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 允许访问Swagger相关接口
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 允许访问认证相关接口
                        .requestMatchers("/auth/**").permitAll()
                        // 允许访问搜索接口（公开接口）
                        .requestMatchers("/search/**").permitAll()
                        // 允许访问歌曲相关接口（公开接口）
                        .requestMatchers("/song/url", "/song/allSongs").permitAll()
                        // 允许访问MV URL接口（公开接口）
                        .requestMatchers("/mv/url").permitAll()
                        // 管理员接口需要ADMIN角色
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // VIP接口需要VIP或更高级别角色
                        .requestMatchers("/vip/**").hasAnyRole("VIP", "SVIP", "ADMIN")
                        // SVIP接口需要SVIP或ADMIN角色
                        .requestMatchers("/svip/**").hasAnyRole("SVIP", "ADMIN")
                        // 其他所有接口都需要认证
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}