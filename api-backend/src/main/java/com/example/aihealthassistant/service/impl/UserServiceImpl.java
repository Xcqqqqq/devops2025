package com.example.aihealthassistant.service.impl;

import com.example.aihealthassistant.dto.UserLoginDTO;
import com.example.aihealthassistant.dto.UserRegisterDTO;
import com.example.aihealthassistant.dto.UserVO;
import com.example.aihealthassistant.entity.User;
import com.example.aihealthassistant.mapper.UserMapper;
import com.example.aihealthassistant.security.JwtUtil;
import com.example.aihealthassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean register(UserRegisterDTO userRegisterDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(userRegisterDTO.getUsername());
        if (existingUser != null) {
            return false;
        }

        // 创建新用户
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        // 设置默认值
        if (userRegisterDTO.getNickname() != null) {
            user.setNickname(userRegisterDTO.getNickname());
        } else {
            user.setNickname(userRegisterDTO.getUsername());
        }
        user.setStatus(1); // 正常状态
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 保存用户
        return userMapper.insert(user) > 0;
    }

    @Override
    public UserVO login(UserLoginDTO userLoginDTO) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(userLoginDTO.getUsername());
        if (user == null) {
            return null;
        }

        // 验证密码
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return null;
        }

        // 更新最后登录时间
        userMapper.updateLastLoginTime(user.getId());

        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());

        // 构建返回对象
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setToken(token); // 设置token到返回对象中
        return userVO;
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }

        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        return userVO;
    }
}