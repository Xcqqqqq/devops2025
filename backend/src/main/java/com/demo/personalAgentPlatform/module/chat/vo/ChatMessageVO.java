package com.demo.personalAgentPlatform.module.chat.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageVO {
    private Long id;
    private Long sessionId;
    private String role; // "user"或"assistant"
    private String content;
    private String model;
    private LocalDateTime createdAt;


}