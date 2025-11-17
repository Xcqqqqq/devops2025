package com.demo.aihealthtool.module.user.service.impl;

import com.demo.aihealthtool.common.exception.BusinessException;
import com.demo.aihealthtool.common.util.JWTUtil;
import com.demo.aihealthtool.module.user.dto.LoginRequest;
import com.demo.aihealthtool.module.user.dto.LoginResponse;
import com.demo.aihealthtool.module.user.dto.RegisterRequest;
import com.demo.aihealthtool.module.user.entity.User;
import com.demo.aihealthtool.module.user.mapper.UserMapper;
import com.demo.aihealthtool.module.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(request.getUsername());
        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setStatus(1); // 正常状态
        user.setIsDeleted(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // 保存用户
        userMapper.insert(user);
    }
    
    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 验证用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("用户账号已被禁用");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 更新最后登录时间
        userMapper.updateLastLoginTime(user.getId(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setStatus(user.getStatus());
        
        response.setUserInfo(userInfo);
        
        return response;
    }
    
    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
}