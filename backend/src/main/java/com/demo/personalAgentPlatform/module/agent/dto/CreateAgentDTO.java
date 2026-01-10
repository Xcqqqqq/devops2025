package com.demo.personalAgentPlatform.module.agent.dto;

import lombok.Data;

@Data
public class CreateAgentDTO {
    private String name;
    private String description;
    private String prompt;
    private Integer isPublic;
}