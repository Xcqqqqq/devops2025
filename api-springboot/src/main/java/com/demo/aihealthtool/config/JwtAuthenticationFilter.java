package com.demo.aihealthtool.config;

import com.demo.aihealthtool.common.util.JWTUtil;
import com.demo.aihealthtool.module.user.entity.User;
import com.demo.aihealthtool.module.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private UserService userService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 从请求头中获取令牌
            String token = getTokenFromRequest(request);
            
            if (token != null && !jwtUtil.isTokenExpired(token)) {
                // 解析令牌获取用户ID
                Long userId = jwtUtil.getUserIdFromToken(token);
                
                // 查询用户信息
                User user = userService.getUserById(userId);
                if (user != null && user.getStatus() == 1) {
                    // 创建认证令牌
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 设置认证信息到上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // 令牌验证失败，不设置认证信息
            logger.error("JWT authentication failed: " + e.getMessage());
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
    
    // 从请求头中获取令牌
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}