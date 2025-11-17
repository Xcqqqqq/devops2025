package com.example.aihealthassistant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.aihealthassistant.mapper")
public class AiHealthAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiHealthAssistantApplication.class, args);
    }

}