package com.demo.aihealthtool.module.chat.dto;

public class CreateSessionDTO {
    private String title;
    private String type; // 可选，默认为"chat"

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}