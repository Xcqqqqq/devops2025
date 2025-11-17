package com.example.aihealthassistant.mapper;

import com.example.aihealthassistant.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    // 根据用户名查询用户
    User selectByUsername(@Param("username") String username);

    // 插入用户
    int insert(User user);

    // 根据ID查询用户
    User selectById(@Param("id") Long id);

    // 更新用户最后登录时间
    int updateLastLoginTime(@Param("id") Long id);
}