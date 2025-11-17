-- 向users表插入默认的两个账户数据
-- 密码均为123456（已BCrypt加密）

-- 替换默认admin用户
REPLACE INTO users (username, password, nickname, email, phone, avatar, gender, status, is_deleted) 
VALUES ('admin', '$2a$10$ix/0Bn.X/A97c5Gl5cW5ju7x6UrtKxpeFh7OXxU4yAT.I8oophcMe', '系统管理员', 'admin@example.com', '13800138001', '/avatars/admin.jpg', 1, 1, 0);

-- 替换默认user用户
REPLACE INTO users (username, password, nickname, email, phone, avatar, gender, status, is_deleted) 
VALUES ('user', '$2a$10$ix/0Bn.X/A97c5Gl5cW5ju7x6UrtKxpeFh7OXxU4yAT.I8oophcMe', '普通用户', 'user@example.com', '13900139001', '/avatars/user.jpg', 2, 1, 0);