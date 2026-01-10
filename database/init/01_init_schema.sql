-- 先删除可能存在的旧表
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `agent`;


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

