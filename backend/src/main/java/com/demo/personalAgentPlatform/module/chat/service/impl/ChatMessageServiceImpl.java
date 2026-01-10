package com.demo.personalAgentPlatform.module.chat.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

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
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    // 日志对象
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageServiceImpl.class);

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatSessionMapper chatSessionMapper;
    
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
            // 创建DeepSeekApiClient实例
            DeepSeekApiClient client = new DeepSeekApiClient();

            // 调用DeepSeek API获取AI回复，传递系统提示词
            String aiResponse = client.chatWithSystemPrompt(agentPrompt, userMessage, "deepseek-chat");

            return aiResponse;
        } catch (IOException e) {
            // 异常处理，返回默认回复
            logger.error("Model API call failed: {}", e.getMessage());
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