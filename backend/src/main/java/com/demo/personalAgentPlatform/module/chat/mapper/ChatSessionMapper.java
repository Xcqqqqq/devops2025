package com.demo.personalAgentPlatform.module.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.demo.personalAgentPlatform.module.chat.entity.ChatSession;

import java.util.List;

@Mapper
public interface ChatSessionMapper {
    // 创建会话
    int insert(ChatSession chatSession);

    // 根据ID获取会话
    ChatSession selectById(@Param("id") Long id);

    // 根据用户ID获取会话列表（分页）
    List<ChatSession> selectByUserId(@Param("userId") Long userId, 
                                   @Param("offset") int offset, 
                                   @Param("limit") int limit);

    // 根据用户ID获取会话总数
    int countByUserId(@Param("userId") Long userId);

    // 更新会话信息
    int update(ChatSession chatSession);

    // 更新会话消息数量
    int updateMessageCount(@Param("id") Long id, @Param("messageCount") Integer messageCount);

    // 删除会话
    int deleteById(@Param("id") Long id);

    // 验证会话是否属于指定用户
    int countByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}