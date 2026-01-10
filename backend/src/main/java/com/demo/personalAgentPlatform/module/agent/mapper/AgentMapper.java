package com.demo.personalAgentPlatform.module.agent.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.demo.personalAgentPlatform.module.agent.entity.Agent;

import java.util.List;

@Mapper
public interface AgentMapper {
    // 插入智能体
    int insert(Agent agent);
    
    // 根据ID查询智能体
    Agent selectById(@Param("id") Long id);
    
    // 根据用户ID查询所有智能体
    List<Agent> selectByUserId(@Param("userId") Long userId);
    
    // 更新智能体
    int update(Agent agent);
    
    // 删除智能体
    int deleteById(@Param("id") Long id);
}