package com.example.aihealthassistant.service;

import com.example.aihealthassistant.dto.UserLoginDTO;
import com.example.aihealthassistant.dto.UserRegisterDTO;
import com.example.aihealthassistant.dto.UserVO;

public interface UserService {

    // 用户注册
    boolean register(UserRegisterDTO userRegisterDTO);

    // 用户登录
    UserVO login(UserLoginDTO userLoginDTO);

    // 根据用户ID获取用户信息
    UserVO getUserInfo(Long userId);
}