package com.demo.personalAgentPlatform.module.chat.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.personalAgentPlatform.module.chat.dto.CreateSessionDTO;
import com.demo.personalAgentPlatform.module.chat.entity.ChatSession;
import com.demo.personalAgentPlatform.module.chat.mapper.ChatSessionMapper;
import com.demo.personalAgentPlatform.module.chat.service.ChatSessionService;
import com.demo.personalAgentPlatform.module.chat.vo.ChatSessionVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Override
    public ChatSessionVO createSession(CreateSessionDTO dto, Long userId) {
        ChatSession chatSession = new ChatSession();
        chatSession.setUserId(userId);
        chatSession.setAgentId(dto.getAgentId());
        chatSession.setTitle(dto.getTitle() != null ? dto.getTitle() : "新会话");
        chatSession.setMessageCount(0);
        chatSession.setCreatedAt(LocalDateTime.now());
        chatSession.setUpdatedAt(LocalDateTime.now());

        chatSessionMapper.insert(chatSession);
        return convertToVO(chatSession);
    }

    @Override
    public ChatSessionVO getSessionById(Long id, Long userId) {
        // 先验证会话是否属于该用户
        if (!validateSessionOwnership(id, userId)) {
            return null;
        }
        ChatSession chatSession = chatSessionMapper.selectById(id);
        return chatSession != null ? convertToVO(chatSession) : null;
    }

    @Override
    public List<ChatSessionVO> getSessionListByUserId(Long userId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<ChatSession> sessions = chatSessionMapper.selectByUserId(userId, offset, size);
        List<ChatSessionVO> sessionVOs = new ArrayList<>();
        for (ChatSession session : sessions) {
            sessionVOs.add(convertToVO(session));
        }
        return sessionVOs;
    }

    @Override
    public boolean deleteSession(Long id, Long userId) {
        // 先验证会话是否属于该用户
        if (!validateSessionOwnership(id, userId)) {
            return false;
        }
        // 直接删除会话（根据数据库表结构，使用物理删除）
        return chatSessionMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateSessionTitle(Long id, String title, Long userId) {
        // 先验证会话是否属于该用户
        if (!validateSessionOwnership(id, userId)) {
            return false;
        }
        ChatSession chatSession = chatSessionMapper.selectById(id);
        if (chatSession != null) {
            chatSession.setTitle(title);
            chatSession.setUpdatedAt(LocalDateTime.now());
            return chatSessionMapper.update(chatSession) > 0;
        }
        return false;
    }

    @Override
    public boolean validateSessionOwnership(Long sessionId, Long userId) {
        return chatSessionMapper.countByIdAndUserId(sessionId, userId) > 0;
    }

    private ChatSessionVO convertToVO(ChatSession chatSession) {
        ChatSessionVO vo = new ChatSessionVO();
        BeanUtils.copyProperties(chatSession, vo);
        return vo;
    }
}