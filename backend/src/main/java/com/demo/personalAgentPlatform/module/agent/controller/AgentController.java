package com.demo.personalAgentPlatform.module.agent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.demo.personalAgentPlatform.common.api.ApiResponse;
import com.demo.personalAgentPlatform.common.api.ResultCode;
import com.demo.personalAgentPlatform.module.agent.dto.CreateAgentDTO;
import com.demo.personalAgentPlatform.module.agent.dto.UpdateAgentDTO;
import com.demo.personalAgentPlatform.module.agent.entity.Agent;
import com.demo.personalAgentPlatform.module.agent.service.AgentService;
import com.demo.personalAgentPlatform.module.user.util.UserUtils;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping("/create")
    public ApiResponse<String> createAgent(@RequestBody CreateAgentDTO request) {
        // 获取当前登录用户的ID
        Long userId = UserUtils.getCurrentUserId();
        // 创建智能体
        agentService.createAgent(request, userId);
        return ApiResponse.ok("创建成功");
    }

    @GetMapping("/get/{id}")
    public ApiResponse<Agent> getAgentById(@PathVariable Long id) {
        Agent agent = agentService.getAgentById(id);
        return ApiResponse.ok(agent);
    }

    @GetMapping("/list")
    public ApiResponse<List<Agent>> listAgentsByUserId() {
        // 用户可以查看自己的智能体和所有公开的智能体
        Long currentUserId = UserUtils.getCurrentUserId();
        // 始终返回当前登录用户的智能体列表（包括自己的和公开的）
        List<Agent> agents = agentService.listAgentsByUserId(currentUserId);
        return ApiResponse.ok(agents);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<String> updateAgent(@PathVariable Long id, @RequestBody UpdateAgentDTO request) {
        // 更新智能体
        agentService.updateAgent(id, request);
        return ApiResponse.ok("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteAgent(@PathVariable Long id) {
        // 删除智能体
        agentService.deleteAgent(id);
        return ApiResponse.ok("删除成功");
    }
}