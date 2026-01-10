package com.demo.personalAgentPlatform.module.chat.service;

import java.util.List;

import com.demo.personalAgentPlatform.module.chat.dto.CreateSessionDTO;
import com.demo.personalAgentPlatform.module.chat.vo.ChatSessionVO;

public interface ChatSessionService {
    // 创建会话
    ChatSessionVO createSession(CreateSessionDTO dto, Long userId);

    // 根据ID获取会话
    ChatSessionVO getSessionById(Long id, Long userId);

    // 获取用户的会话列表（分页）
    List<ChatSessionVO> getSessionListByUserId(Long userId, Integer page, Integer size);

    // 删除会话
    boolean deleteSession(Long id, Long userId);

    // 更新会话标题
    boolean updateSessionTitle(Long id, String title, Long userId);

    // 验证会话是否属于指定用户
    boolean validateSessionOwnership(Long sessionId, Long userId);
}