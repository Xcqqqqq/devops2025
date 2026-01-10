package com.demo.personalAgentPlatform.module.chat.dto;

import lombok.Data;

@Data
public class CreateSessionDTO {
    private String title;
    private String type; // 可选，默认为"chat"


}