package com.demo.personalAgentPlatform.module.user.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.demo.personalAgentPlatform.module.user.entity.User;
import com.demo.personalAgentPlatform.module.user.service.UserService;

@Component
public class UserUtils {

    private static UserService userService;

    @Autowired
    public UserUtils(UserService userService) {
        UserUtils.userService = userService;
    }

    // 获取当前登录用户的用户名
    public static String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // 获取当前登录用户的ID
    public static Long getCurrentUserId() {
        String username = getCurrentUsername();
        // 匿名用户或未登录时返回null
        if (username == null || "anonymousUser".equals(username)) {
            return null;
        }
        User user = userService.getUserByUsername(username);
        return user != null ? user.getId() : null;
    }

    // 获取当前登录用户的完整信息
    public static User getCurrentUser() {
        String username = getCurrentUsername();
        return userService.getUserByUsername(username);
    }
}