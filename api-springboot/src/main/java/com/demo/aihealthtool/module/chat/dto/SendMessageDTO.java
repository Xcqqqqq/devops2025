package com.demo.aihealthtool.module.chat.dto;

public class SendMessageDTO {
    private Long sessionId; // 会话ID
    private String content; // 消息内容

    // Getters and Setters
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}