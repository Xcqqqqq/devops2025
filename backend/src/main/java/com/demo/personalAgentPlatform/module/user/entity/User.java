package com.demo.personalAgentPlatform.module.user.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Integer permission; // 0-普通用户 1-管理员
    private String avatar; // 头像
}