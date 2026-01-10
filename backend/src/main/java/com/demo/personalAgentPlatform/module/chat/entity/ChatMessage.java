package com.demo.personalAgentPlatform.module.chat.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessage {
    private Long id;
    private Long sessionId; // 会话ID
    private String role; // 消息角色，"user"或"assistant"
    private String content; // 消息内容
    private String model; // 模型名称
    private LocalDateTime time; // 创建时间
}