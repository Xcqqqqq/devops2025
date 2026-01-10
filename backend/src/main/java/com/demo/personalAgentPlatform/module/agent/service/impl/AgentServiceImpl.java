package com.demo.personalAgentPlatform.module.agent.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.personalAgentPlatform.common.exception.BusinessException;
import com.demo.personalAgentPlatform.module.agent.dto.CreateAgentDTO;
import com.demo.personalAgentPlatform.module.agent.dto.UpdateAgentDTO;
import com.demo.personalAgentPlatform.module.agent.entity.Agent;
import com.demo.personalAgentPlatform.module.agent.mapper.AgentMapper;
import com.demo.personalAgentPlatform.module.agent.service.AgentService;

import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentMapper agentMapper;

    @Override
    @Transactional
    public void createAgent(CreateAgentDTO request, Long userId) {
        // 创建智能体对象
        Agent agent = new Agent();
        BeanUtils.copyProperties(request, agent);
        agent.setUserId(userId);
        // 设置默认值
        if (agent.getIsPublic() == null) {
            agent.setIsPublic(0);
        }
        // 保存到数据库
        agentMapper.insert(agent);
    }

    @Override
    public Agent getAgentById(Long id) {
        Agent agent = agentMapper.selectById(id);
        if (agent == null) {
            throw new BusinessException("智能体不存在");
        }
        return agent;
    }

    @Override
    public List<Agent> listAgentsByUserId(Long userId) {
        return agentMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public void updateAgent(Long id, UpdateAgentDTO request) {
        // 检查智能体是否存在
        Agent existingAgent = agentMapper.selectById(id);
        if (existingAgent == null) {
            throw new BusinessException("智能体不存在");
        }
        // 更新智能体信息
        existingAgent.setDescription(request.getDescription());
        existingAgent.setPrompt(request.getPrompt());
        agentMapper.update(existingAgent);
    }

    @Override
    @Transactional
    public void deleteAgent(Long id) {
        // 检查智能体是否存在
        Agent existingAgent = agentMapper.selectById(id);
        if (existingAgent == null) {
            throw new BusinessException("智能体不存在");
        }
        // 删除智能体
        agentMapper.deleteById(id);
    }
}