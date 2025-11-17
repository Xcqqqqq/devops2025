# 数据库设计



## 2. health_records 表（健康档案表）
存储用户的个人健康信息

```sql
CREATE TABLE health_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    gender VARCHAR(10) COMMENT '性别（男/女/其他）',
    age INT COMMENT '年龄',
    height DECIMAL(5,2) COMMENT '身高（cm）',
    weight DECIMAL(5,2) COMMENT '体重（kg）',
    blood_type VARCHAR(5) COMMENT '血型',
    allergies TEXT COMMENT '过敏史',
    medical_history TEXT COMMENT '既往病史',
    family_history TEXT COMMENT '家族病史',
    medication_record TEXT COMMENT '用药记录',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

示例数据：
```
id: 1
user_id: 1
gender: "男"
age: 35
height: 175.00
weight: 70.50
blood_type: "A型"
allergies: "青霉素过敏"
medical_history: "无重大疾病史"
family_history: "父亲高血压，母亲健康"
medication_record: "偶尔服用维生素C"
created_at: "2024-01-15 10:35:00"
updated_at: "2024-01-18 16:40:00"
```

## 3. chat_messages 表（聊天记录表）
存储用户与AI助手的对话历史

```sql
CREATE TABLE chat_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    session_id VARCHAR(100) NOT NULL COMMENT '会话ID',
    role VARCHAR(20) NOT NULL COMMENT '角色（user/assistant/system）',
    content TEXT NOT NULL COMMENT '消息内容',
    message_type VARCHAR(20) DEFAULT 'text' COMMENT '消息类型（text/image/file）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    model_used VARCHAR(50) COMMENT '使用的AI模型',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_session (session_id),
    INDEX idx_user_time (user_id, created_at)
);
```

示例数据：
```
id: 1
user_id: 1
session_id: "session_1705432800000"
role: "user"
content: "我最近总是感到头晕，是什么原因？"
message_type: "text"
created_at: "2024-01-16 09:30:00"
model_used: "glm-4"

id: 2
user_id: 1
session_id: "session_1705432800000"
role: "assistant"
content: "头晕可能由多种原因引起，常见的包括：1. 血压异常 2. 贫血 3. 睡眠不足 4. 颈椎病等。建议您：保持充足睡眠，避免长时间低头，如症状持续请及时就医。"
message_type: "text"
created_at: "2024-01-16 09:31:20"
model_used: "glm-4"
```

## 4. health_reports 表（健康报告表）
存储基于AI分析生成的健康报告

```sql
CREATE TABLE health_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报告ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '报告标题',
    summary TEXT COMMENT '报告摘要',
    content TEXT NOT NULL COMMENT '报告详细内容',
    generated_by VARCHAR(50) COMMENT '生成模型',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

示例数据：
```
id: 1
user_id: 1
title: "2024年度健康评估报告"
summary: "综合评估：健康状况良好，建议加强运动锻炼。"
content: "详细分析：根据您提供的信息，您的BMI指数为23.1，属于正常范围..."
generated_by: "glm-4"
created_at: "2024-01-20 15:30:00"
```

## 5. user_settings 表（用户设置表）
存储用户个性化设置

```sql
CREATE TABLE user_settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '设置ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    preferred_model VARCHAR(50) DEFAULT 'glm-4' COMMENT '偏好的AI模型',
    language VARCHAR(20) DEFAULT 'zh-CN' COMMENT '语言设置',
    notification_enabled BOOLEAN DEFAULT TRUE COMMENT '是否开启通知',
    privacy_level TINYINT DEFAULT 1 COMMENT '隐私级别（1-低 2-中 3-高）',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

示例数据：
```
id: 1
user_id: 1
preferred_model: "glm-4"
language: "zh-CN"
notification_enabled: true
privacy_level: 2
updated_at: "2024-01-15 10:40:00"
```

## 表间关系
1. users表是核心表，与其他表有一对多的关系
2. 一个用户可以有一个健康档案（health_records）
3. 一个用户可以有多条聊天记录（chat_messages）
4. 一个用户可以有多份健康报告（health_reports）
5. 一个用户只有一组个性化设置（user_settings）
