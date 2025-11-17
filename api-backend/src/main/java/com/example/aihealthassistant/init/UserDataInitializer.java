package com.example.aihealthassistant.init;

import com.example.aihealthassistant.entity.User;
import com.example.aihealthassistant.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserDataInitializer implements ApplicationRunner {

    @Autowired
    private UserMapper userMapper;

    @Value("${init.user.admin.username}")
    private String adminUsername;

    @Value("${init.user.admin.password}")
    private String adminPassword;

    @Value("${init.user.user.username}")
    private String userUsername;

    @Value("${init.user.user.password}")
    private String userPassword;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 创建admin用户
        createDefaultUser(adminUsername, adminPassword, "管理员");
        // 创建user用户
        createDefaultUser(userUsername, userPassword, "普通用户");
    }

    private void createDefaultUser(String username, String password, String nickname) {
        User existingUser = userMapper.selectByUsername(username);
        if (existingUser == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setNickname(nickname);
            user.setStatus(1);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.insert(user);
            System.out.println("Created default user: " + username);
        }
    }
}