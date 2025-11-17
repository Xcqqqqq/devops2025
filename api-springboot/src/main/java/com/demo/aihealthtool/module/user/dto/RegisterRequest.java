package com.demo.aihealthtool.module.user.dto;

public class RegisterRequest {
    private String username;
    private String password;
    private String nickname;
    
    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }
}