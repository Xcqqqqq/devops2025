package com.demo.personalAgentPlatform.module.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.demo.personalAgentPlatform.common.api.ApiResponse;
import com.demo.personalAgentPlatform.module.chat.dto.CreateSessionDTO;
import com.demo.personalAgentPlatform.module.chat.dto.SendMessageDTO;
import com.demo.personalAgentPlatform.module.chat.service.ChatMessageService;
import com.demo.personalAgentPlatform.module.chat.service.ChatSessionService;
import com.demo.personalAgentPlatform.module.chat.vo.ChatMessageVO;
import com.demo.personalAgentPlatform.module.chat.vo.ChatSessionVO;
import com.demo.personalAgentPlatform.module.user.util.UserUtils;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    // 日志对象
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatSessionService chatSessionService;

    @Autowired
    private ChatMessageService chatMessageService;

    // 创建会话
    @PostMapping("/sessions")
    public ApiResponse<ChatSessionVO> createSession(@RequestBody CreateSessionDTO dto) {
        Long userId = UserUtils.getCurrentUserId();
        ChatSessionVO sessionVO = chatSessionService.createSession(dto, userId);
        return ApiResponse.ok(sessionVO);
    }

    // 获取会话列表（分页）
    @GetMapping("/sessions")
    public ApiResponse<List<ChatSessionVO>> getSessionList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        Long userId = UserUtils.getCurrentUserId();
        // logger.info("calling getSessionListByUserId with userId: {}, page: {}, size: {}\n", userId, page, size);
        List<ChatSessionVO> sessions = chatSessionService.getSessionListByUserId(userId, page, size);
        return ApiResponse.ok(sessions);
    }

    // 获取会话详情
    @GetMapping("/sessions/{id}")
    public ApiResponse<ChatSessionVO> getSessionDetail(@PathVariable Long id) {
        Long userId = UserUtils.getCurrentUserId();
        ChatSessionVO sessionVO = chatSessionService.getSessionById(id, userId);
        if (sessionVO == null) {
            return ApiResponse.fail(404, "会话不存在或无权访问");
        }
        return ApiResponse.ok(sessionVO);
    }

    // 删除会话
    @DeleteMapping("/sessions/{id}")
    public ApiResponse<Boolean> deleteSession(@PathVariable Long id) {
        Long userId = UserUtils.getCurrentUserId();
        boolean result = chatSessionService.deleteSession(id, userId);
        if (!result) {
            return ApiResponse.fail(404, "会话不存在或无权访问");
        }
        return ApiResponse.ok(true);
    }

    // 发送消息
    @PostMapping("/messages")
    public ApiResponse<ChatMessageVO> sendMessage(@RequestBody SendMessageDTO dto) {
        try {
            Long userId = UserUtils.getCurrentUserId();
            ChatMessageVO messageVO = chatMessageService.sendMessage(dto, userId);
            return ApiResponse.ok(messageVO);
        } catch (RuntimeException e) {
            return ApiResponse.fail(400, e.getMessage());
        }
    }

    // 获取消息历史（分页）
    @GetMapping("/messages")
    public ApiResponse<List<ChatMessageVO>> getMessageHistory(
            @RequestParam Long sessionId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "50") Integer size) {
        try {
            Long userId = UserUtils.getCurrentUserId();
            List<ChatMessageVO> messages = chatMessageService.getMessageListBySessionId(sessionId, userId, page, size);
            return ApiResponse.ok(messages);
        } catch (RuntimeException e) {
            return ApiResponse.fail(400, e.getMessage());
        }
    }

    // 获取会话的消息列表（另一种路径方式，与上面的方法功能相同）
    @GetMapping("/sessions/{sessionId}/messages")
    public ApiResponse<List<ChatMessageVO>> getSessionMessages(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "50") Integer size) {
        try {
            Long userId = UserUtils.getCurrentUserId();
            List<ChatMessageVO> messages = chatMessageService.getMessageListBySessionId(sessionId, userId, page, size);
            return ApiResponse.ok(messages);
        } catch (RuntimeException e) {
            return ApiResponse.fail(400, e.getMessage());
        }
    }
}