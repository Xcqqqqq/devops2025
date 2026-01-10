package com.demo.personalAgentPlatform.module.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.demo.personalAgentPlatform.module.user.entity.User;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    User selectByUsername(@Param("username") String username);
    
    // 根据ID查询用户
    User selectById(@Param("id") Long id);
    
    // 插入用户
    int insert(User user);
    
    // 更新用户
    int update(User user);
}