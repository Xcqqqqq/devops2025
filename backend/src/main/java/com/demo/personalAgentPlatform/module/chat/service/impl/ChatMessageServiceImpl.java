package com.demo.personalAgentPlatform.module.chat.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import com.demo.personalAgentPlatform.module.chat.dto.SendMessageDTO;
import com.demo.personalAgentPlatform.module.chat.entity.ChatMessage;
import com.demo.personalAgentPlatform.module.chat.entity.ChatSession;
import com.demo.personalAgentPlatform.module.chat.mapper.ChatMessageMapper;
import com.demo.personalAgentPlatform.module.chat.mapper.ChatSessionMapper;
import com.demo.personalAgentPlatform.module.chat.service.ChatMessageService;
import com.demo.personalAgentPlatform.module.chat.vo.ChatMessageVO;
import com.demo.personalAgentPlatform.module.agent.service.AgentService;
import com.demo.personalAgentPlatform.module.agent.entity.Agent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private AgentService agentService;

    @Override
    @Transactional
    public ChatMessageVO sendMessage(SendMessageDTO dto, Long userId) {
        // 1. 验证会话是否属于该用户
        if (chatSessionMapper.countByIdAndUserId(dto.getSessionId(), userId) == 0) {
            throw new RuntimeException("会话不存在或无权访问");
        }

        // 2. 保存用户消息
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSessionId(dto.getSessionId());
        userMessage.setRole("user");
        userMessage.setContent(dto.getContent());
        userMessage.setModel("deepseek");
        userMessage.setTime(LocalDateTime.now());
        chatMessageMapper.insert(userMessage);

        // 3. 获取会话对应的agent信息
        ChatSession chatSession = chatSessionMapper.selectById(dto.getSessionId());
        Agent agent = agentService.getAgentById(chatSession.getAgentId());
        
        // 4. 调用模型API获取AI回复，传递agent的prompt
        String aiResponse = callModelAPI(dto.getContent(), agent.getPrompt());

        // 4. 保存AI回复消息
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setSessionId(dto.getSessionId());
        aiMessage.setRole("assistant");
        aiMessage.setContent(aiResponse);
        aiMessage.setModel("deepseek");
        aiMessage.setTime(LocalDateTime.now());
        chatMessageMapper.insert(aiMessage);

        // 5. 更新会话信息（消息数量和更新时间）
        updateSessionInfo(dto.getSessionId());

        // 6. 返回AI回复消息
        return convertToVO(aiMessage);
    }

    @Override
    public List<ChatMessageVO> getMessageListBySessionId(Long sessionId, Long userId, Integer page, Integer size) {
        // 验证会话是否属于该用户
        if (chatSessionMapper.countByIdAndUserId(sessionId, userId) == 0) {
            throw new RuntimeException("会话不存在或无权访问");
        }

        int offset = (page - 1) * size;
        List<ChatMessage> messages = chatMessageMapper.selectBySessionId(sessionId, offset, size);
        List<ChatMessageVO> messageVOs = new ArrayList<>();
        for (ChatMessage message : messages) {
            messageVOs.add(convertToVO(message));
        }
        return messageVOs;
    }

    @Override
    @Transactional
    public boolean deleteMessagesBySessionId(Long sessionId, Long userId) {
        // 验证会话是否属于该用户
        if (chatSessionMapper.countByIdAndUserId(sessionId, userId) == 0) {
            return false;
        }

        // 删除会话的所有消息
        int result = chatMessageMapper.deleteBySessionId(sessionId);

        // 更新会话的消息数量
        ChatSession chatSession = chatSessionMapper.selectById(sessionId);
        if (chatSession != null) {
            chatSession.setMessageCount(0);
            chatSessionMapper.update(chatSession);
        }

        return result > 0;
    }

    /**
     * 调用模型API获取AI回复
     * @param userMessage 用户消息内容
     * @param agentPrompt 智能体提示词
     * @return AI回复内容
     */
    private String callModelAPI(String userMessage, String agentPrompt) {
        try {
            // 模型API地址（示例地址，实际使用时需要替换）
            String apiUrl = "https://api.deepseek.com";
            
            // API密钥
            String apiKey = "sk-4fb65d47751743799deb8bca1b0f0eba";
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            // 设置请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-v3.2");
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", agentPrompt);
            messages.add(systemMessage);
            
            Map<String, String> userContent = new HashMap<>();
            userContent.put("role", "user");
            userContent.put("content", userMessage);
            messages.add(userContent);
            
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 500);
            
            // 创建请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 发送请求
            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
            );
            
            // 解析响应
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    if (message != null) {
                        return (String) message.get("content");
                    }
                }
            }
            
            // 如果API调用失败，返回默认回复
            return generateAIResponse(userMessage);
        } catch (Exception e) {
            // 异常处理，返回默认回复
            e.printStackTrace();
            return generateAIResponse(userMessage);
        }
    }
    
    /**
     * 更新会话信息（消息数量和更新时间）
     * @param sessionId 会话ID
     */
    private void updateSessionInfo(Long sessionId) {
        // 获取消息总数
        int messageCount = chatMessageMapper.countBySessionId(sessionId);
        // 更新会话的消息数量
        chatSessionMapper.updateMessageCount(sessionId, messageCount);
        
        // 获取最新的会话信息
        ChatSession chatSession = chatSessionMapper.selectById(sessionId);
        if (chatSession != null) {
            // 更新会话的更新时间
            chatSession.setUpdatedAt(LocalDateTime.now());
            chatSessionMapper.update(chatSession);
        }
    }
    
    @Override
    public String generateAIResponse(String userMessage) {
        // 模拟AI回复，当API调用失败时使用
        List<String> responses = new ArrayList<>();
        responses.add("模型调用失败，这是默认回复。");

        // 随机选择一个回复
        Random random = new Random();
        return responses.get(random.nextInt(responses.size()));
    }
    
    private ChatMessageVO convertToVO(ChatMessage chatMessage) {
        ChatMessageVO vo = new ChatMessageVO();
        BeanUtils.copyProperties(chatMessage, vo);
        return vo;
    }
}