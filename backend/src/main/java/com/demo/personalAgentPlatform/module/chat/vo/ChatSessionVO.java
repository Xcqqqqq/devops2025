package com.demo.personalAgentPlatform.module.chat.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatSessionVO {
    private Long id;
    private Long userId;
    private Long agentId;
    private String title;
    private Integer messageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}