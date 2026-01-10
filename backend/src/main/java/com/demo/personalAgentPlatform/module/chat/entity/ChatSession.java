package com.demo.personalAgentPlatform.module.chat.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatSession {
    private Long id;
    private Long userId;
    private Long agentId; // 智能体ID
    private String title;
    private Integer messageCount; // 会话中的消息数量
    private LocalDateTime createdAt; // 会话创建时间
    private LocalDateTime updatedAt; // 会话更新时间
}