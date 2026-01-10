-- 向users表插入默认的两个账户数据
-- 密码均为123456（已BCrypt加密）

-- 替换默认admin用户
REPLACE INTO users (id, username, password, nickname, permission, avatar) 
VALUES (1, 'admin', '$2a$10$ix/0Bn.X/A97c5Gl5cW5ju7x6UrtKxpeFh7OXxU4yAT.I8oophcMe', '系统管理员', 1, '/avatars/admin.jpg');

-- 替换默认user用户
REPLACE INTO users (id, username, password, nickname, permission, avatar) 
VALUES (2, 'user', '$2a$10$ix/0Bn.X/A97c5Gl5cW5ju7x6UrtKxpeFh7OXxU4yAT.I8oophcMe', '普通用户', 0, '/avatars/user.jpg');

-- 向agent表插入默认的智能体数据
INSERT INTO agent (id, userId, name, description, prompt, isPublic)
VALUES (1, 1, 'default_agent', '默认智能体', '你是一个智能体，你可以回答用户的任何问题。', 1);
