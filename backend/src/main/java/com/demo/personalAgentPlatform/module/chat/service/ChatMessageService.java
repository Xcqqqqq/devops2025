package com.demo.personalAgentPlatform.module.chat.service;

import java.util.List;

import com.demo.personalAgentPlatform.module.chat.dto.SendMessageDTO;
import com.demo.personalAgentPlatform.module.chat.vo.ChatMessageVO;

public interface ChatMessageService {
    // 发送消息（用户消息并生成AI回复）
    ChatMessageVO sendMessage(SendMessageDTO dto, Long userId);

    // 获取会话的消息列表（分页）
    List<ChatMessageVO> getMessageListBySessionId(Long sessionId, Long userId, Integer page, Integer size);

    // 删除会话的所有消息
    boolean deleteMessagesBySessionId(Long sessionId, Long userId);

    // 生成AI回复（模拟，不调用真实模型）
    String generateAIResponse(String userMessage);
}