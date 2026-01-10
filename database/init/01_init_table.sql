-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库（指定字符集）
CREATE DATABASE IF NOT EXISTS personal_agent_db
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE personal_agent_db;

-- 先删除可能存在的旧表
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `agent`;
DROP TABLE IF EXISTS `chat_session`;
DROP TABLE IF EXISTS `chat_message`;

-- 创建统一的users表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    permission INT DEFAULT 0 COMMENT '权限（0-普通用户 1-管理员）',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建agent表
CREATE TABLE agent (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '智能体ID',
    userId BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '智能体名称',
    description TEXT COMMENT '智能体描述',
    prompt TEXT COMMENT '智能体提示词',
    isPublic INT DEFAULT 0 COMMENT '是否公开（0-否 1-是）',
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能体表';

-- 创建chat_session表
CREATE TABLE chat_session (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    userId BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(255) COMMENT '会话标题',
    agentId BIGINT NOT NULL COMMENT '智能体ID',
    messageCount INT DEFAULT 0 COMMENT '会话中的消息数量',
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '会话创建时间',
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '会话更新时间',
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (agentId) REFERENCES agent(id) ON DELETE CASCADE,
    INDEX idx_userId (userId),
    INDEX idx_agentId (agentId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天会话表';    

-- 创建chat_message表
CREATE TABLE chat_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    sessionId BIGINT NOT NULL COMMENT '会话ID',
    role VARCHAR(50) NOT NULL COMMENT '消息角色',
    content TEXT COMMENT '消息内容',
    model VARCHAR(50) COMMENT '模型名称',
    time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '消息时间',
    FOREIGN KEY (sessionId) REFERENCES chat_session(id) ON DELETE CASCADE,
    INDEX idx_sessionId (sessionId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';