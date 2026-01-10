package com.demo.personalAgentPlatform.module.chat.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatSession {
    private Long id;
    private Long userId;
    private String title;
    private String type; // 会话类型，默认值为"chat"
    private Integer status; // 0 已结束 1 进行中
    private Integer messageCount; // 会话中的消息数量
    private LocalDateTime createdAt; // 会话创建时间
    private LocalDateTime updatedAt; // 会话更新时间
    private Integer isDeleted; // 逻辑删除标记
    private LocalDateTime lastMessageTime; // 最后消息时间


}