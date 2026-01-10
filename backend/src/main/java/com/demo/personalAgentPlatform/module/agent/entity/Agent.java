package com.demo.personalAgentPlatform.module.agent.entity;

import lombok.Data;

@Data
public class Agent {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private String prompt;
    private Integer isPublic;
}