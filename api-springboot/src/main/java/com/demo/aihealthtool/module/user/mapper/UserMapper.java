package com.demo.aihealthtool.module.user.mapper;

import com.demo.aihealthtool.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    
    // 更新最后登录时间
    void updateLastLoginTime(@Param("id") Long id, @Param("lastLoginTime") String lastLoginTime);
}