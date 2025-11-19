package com.demo.aihealthtool.module.chat.mapper;

import com.demo.aihealthtool.module.chat.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    // 插入消息
    int insert(ChatMessage chatMessage);

    // 根据ID获取消息
    ChatMessage selectById(@Param("id") Long id);

    // 根据会话ID获取消息列表（分页）
    List<ChatMessage> selectBySessionId(@Param("sessionId") Long sessionId, 
                                      @Param("offset") int offset, 
                                      @Param("limit") int limit);

    // 根据会话ID获取消息总数
    int countBySessionId(@Param("sessionId") Long sessionId);

    // 根据会话ID删除所有消息
    int deleteBySessionId(@Param("sessionId") Long sessionId);
}