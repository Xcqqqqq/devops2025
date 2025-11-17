package com.demo.aihealthtool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.demo.aihealthtool.module.*.mapper")
public class AiHealthToolApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AiHealthToolApplication.class, args);
    }
}