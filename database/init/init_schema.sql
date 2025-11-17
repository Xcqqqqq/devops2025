-- 创建users表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    gender INT DEFAULT 0 COMMENT '性别（0-未知 1-男 2-女）',
    birthday DATE COMMENT '生日',
    status INT DEFAULT 1 COMMENT '状态（0-禁用 1-正常）',
    last_login_time DATETIME COMMENT '最后登录时间',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    is_deleted INT DEFAULT 0 COMMENT '是否删除（0-未删除 1-已删除）',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';