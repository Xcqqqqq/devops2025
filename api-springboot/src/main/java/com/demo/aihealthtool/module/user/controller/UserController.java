package com.demo.aihealthtool.module.user.controller;

import com.demo.aihealthtool.common.api.ApiResponse;
import com.demo.aihealthtool.module.user.dto.LoginRequest;
import com.demo.aihealthtool.module.user.dto.LoginResponse;
import com.demo.aihealthtool.module.user.dto.RegisterRequest;
import com.demo.aihealthtool.module.user.entity.User;
import com.demo.aihealthtool.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ApiResponse.ok("注册成功");
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ApiResponse.ok(response);
    }

    @GetMapping("/info")
    public ApiResponse<User> getUserInfo() {
        // 从Security上下文获取当前登录用户信息
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        return ApiResponse.ok(user);
    }
}