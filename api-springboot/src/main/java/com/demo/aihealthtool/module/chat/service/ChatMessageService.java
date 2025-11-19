package com.demo.aihealthtool.module.chat.service;

import com.demo.aihealthtool.module.chat.dto.SendMessageDTO;
import com.demo.aihealthtool.module.chat.vo.ChatMessageVO;

import java.util.List;

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