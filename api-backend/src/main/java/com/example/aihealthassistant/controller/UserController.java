package com.example.aihealthassistant.controller;

import com.example.aihealthassistant.common.Result;
import com.example.aihealthassistant.dto.UserLoginDTO;
import com.example.aihealthassistant.dto.UserRegisterDTO;
import com.example.aihealthassistant.dto.UserVO;
import com.example.aihealthassistant.security.JwtUtil;
import com.example.aihealthassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 用户注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        boolean success = userService.register(userRegisterDTO);
        if (success) {
            return Result.success("注册成功");
        } else {
            return Result.error(400, "用户名已存在");
        }
    }

    // 用户登录
    @PostMapping("/login")
    public Result<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserVO userVO = userService.login(userLoginDTO);
        if (userVO != null) {
            return Result.success(userVO);
        } else {
            return Result.error(400, "用户名或密码错误");
        }
    }

    // 获取当前用户信息
    @GetMapping("/info")
    public Result<?> getUserInfo(HttpServletRequest request) {
        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "无效的token");
        }

        // 从token中获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(token);
        UserVO userVO = userService.getUserInfo(userId);
        if (userVO != null) {
            return Result.success(userVO);
        } else {
            return Result.error(404, "用户不存在");
        }
    }
}