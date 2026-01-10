package com.demo.personalAgentPlatform.module.chat.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.personalAgentPlatform.module.chat.dto.SendMessageDTO;
import com.demo.personalAgentPlatform.module.chat.entity.ChatMessage;
import com.demo.personalAgentPlatform.module.chat.entity.ChatSession;
import com.demo.personalAgentPlatform.module.chat.mapper.ChatMessageMapper;
import com.demo.personalAgentPlatform.module.chat.mapper.ChatSessionMapper;
import com.demo.personalAgentPlatform.module.chat.service.ChatMessageService;
import com.demo.personalAgentPlatform.module.chat.vo.ChatMessageVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Override
    @Transactional
    public ChatMessageVO sendMessage(SendMessageDTO dto, Long userId) {
        // 验证会话是否属于该用户
        if (chatSessionMapper.countByIdAndUserId(dto.getSessionId(), userId) == 0) {
            throw new RuntimeException("会话不存在或无权访问");
        }

        // 保存用户消息
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSessionId(dto.getSessionId());
        userMessage.setRole("user");
        userMessage.setContent(dto.getContent());
        userMessage.setModel("health-assistant");
        userMessage.setCreatedAt(LocalDateTime.now());
        chatMessageMapper.insert(userMessage);

        // 生成AI回复
        String aiResponse = generateAIResponse(dto.getContent());

        // 保存AI回复消息
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setSessionId(dto.getSessionId());
        aiMessage.setRole("assistant");
        aiMessage.setContent(aiResponse);
        aiMessage.setModel("health-assistant");
        aiMessage.setCreatedAt(LocalDateTime.now());
        chatMessageMapper.insert(aiMessage);

        // 更新会话信息
        updateSessionInfo(dto.getSessionId());

        // 返回AI回复消息
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

    @Override
    public String generateAIResponse(String userMessage) {
        // 模拟AI回复，不调用真实模型
        List<String> responses = new ArrayList<>();
        responses.add("感谢您的咨询。根据您的描述，我建议您保持良好的作息习惯，注意饮食均衡。");
        responses.add("您好！很高兴为您提供健康建议。建议您每天进行适量的运动，保持愉悦的心情。");
        responses.add("感谢您的提问。从健康角度来看，充足的睡眠对身体非常重要，建议您每晚保证7-8小时的睡眠。");
        responses.add("您好！您的问题很有价值。饮食方面，建议多摄入蔬菜水果，减少油腻食物的摄入。");
        responses.add("感谢您的咨询。如果您有任何不适，建议及时就医，听取专业医生的建议。");
        responses.add("您好！保持水分摄入对健康很重要，建议每天喝够8杯水。");

        // 随机选择一个回复
        Random random = new Random();
        return responses.get(random.nextInt(responses.size()));
    }

    private void updateSessionInfo(Long sessionId) {
        // 获取消息总数
        int messageCount = chatMessageMapper.countBySessionId(sessionId);
        // 更新会话的消息数量和最后消息时间
        chatSessionMapper.updateMessageCount(sessionId, messageCount);
        chatSessionMapper.updateLastMessageTime(sessionId, LocalDateTime.now());
    }

    private ChatMessageVO convertToVO(ChatMessage chatMessage) {
        ChatMessageVO vo = new ChatMessageVO();
        BeanUtils.copyProperties(chatMessage, vo);
        return vo;
    }
}