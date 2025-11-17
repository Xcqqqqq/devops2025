package com.demo.aihealthtool.module.user.service;

import com.demo.aihealthtool.module.user.dto.LoginRequest;
import com.demo.aihealthtool.module.user.dto.LoginResponse;
import com.demo.aihealthtool.module.user.dto.RegisterRequest;
import com.demo.aihealthtool.module.user.entity.User;

public interface UserService {
    // 用户注册
    void register(RegisterRequest request);
    
    // 用户登录
    LoginResponse login(LoginRequest request);
    
    // 根据用户名查询用户
    User getUserByUsername(String username);
    
    // 根据ID查询用户
    User getUserById(Long id);
}