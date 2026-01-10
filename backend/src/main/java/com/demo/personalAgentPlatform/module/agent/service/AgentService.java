package com.demo.personalAgentPlatform.module.agent.service;

import java.util.List;

import com.demo.personalAgentPlatform.module.agent.dto.CreateAgentDTO;
import com.demo.personalAgentPlatform.module.agent.dto.UpdateAgentDTO;
import com.demo.personalAgentPlatform.module.agent.entity.Agent;

public interface AgentService {
    // 创建智能体
    void createAgent(CreateAgentDTO request, Long userId);
    
    // 根据ID查询智能体
    Agent getAgentById(Long id);
    
    // 根据用户ID查询所有智能体
    List<Agent> listAgentsByUserId(Long userId);
    
    // 更新智能体
    void updateAgent(Long id, UpdateAgentDTO request);
    
    // 删除智能体
    void deleteAgent(Long id);
}