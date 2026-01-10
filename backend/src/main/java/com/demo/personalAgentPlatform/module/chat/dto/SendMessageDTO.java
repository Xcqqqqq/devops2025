package com.demo.personalAgentPlatform.module.chat.dto;

import lombok.Data;

@Data
public class SendMessageDTO {
    private Long sessionId; // 会话ID
    private String content; // 消息内容
}