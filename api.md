# AI健康助手API文档

## 1. 简介
本文档记录了AI健康助手的所有API接口信息，包括用户管理和聊天功能相关的接口。

## 2. 用户相关接口

### 2.1 用户注册
- **接口地址**：`/api/user/register`
- **请求方法**：`POST`
- **请求体**：
  ```json
  {
    "username": "用户名",
    "password": "密码",
    "email": "邮箱",
    "phone": "手机号",
    "nickname": "昵称"
  }
  ```
- **响应**：
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": "注册成功"
  }
  ```

### 2.2 用户登录
- **接口地址**：`/api/user/login`
- **请求方法**：`POST`
- **请求体**：
  ```json
  {
    "username": "用户名",
    "password": "密码"
  }
  ```
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "token": "JWT令牌",
      "userInfo": {
        "id": 1,
        "username": "用户名",
        "nickname": "昵称",
        "email": "邮箱",
        "phone": "手机号",
        "avatar": "头像地址",
        "gender": 1,
        "age": 25,
        "height": 175,
        "weight": 65,
        "bloodType": "A",
        "medicalHistory": "病史",
        "createdAt": "2023-01-01T00:00:00",
        "updatedAt": "2023-01-01T00:00:00"
      }
    }
  }
  ```

### 2.3 获取用户信息
- **接口地址**：`/api/user/info`
- **请求方法**：`GET`
- **请求头**：需要JWT认证
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "username": "用户名",
      "nickname": "昵称",
      "email": "邮箱",
      "phone": "手机号",
      "avatar": "头像地址",
      "gender": 1,
      "age": 25,
      "height": 175,
      "weight": 65,
      "bloodType": "A",
      "medicalHistory": "病史",
      "createdAt": "2023-01-01T00:00:00",
      "updatedAt": "2023-01-01T00:00:00"
    }
  }
  ```

## 3. 聊天会话相关接口

### 3.1 创建会话
- **接口地址**：`/api/chat/sessions`
- **请求方法**：`POST`
- **请求头**：需要JWT认证
- **请求体**：
  ```json
  {
    "title": "会话标题"
  }
  ```
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "会话标题",
      "messageCount": 0,
      "lastMessage": null,
      "lastMessageAt": null,
      "createdAt": "2023-01-01T00:00:00",
      "updatedAt": "2023-01-01T00:00:00"
    }
  }
  ```

### 3.2 获取会话列表
- **接口地址**：`/api/chat/sessions`
- **请求方法**：`GET`
- **请求头**：需要JWT认证
- **请求参数**：
  - `page`: 页码，默认1
  - `size`: 每页数量，默认20
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": [
      {
        "id": 1,
        "userId": 1,
        "title": "会话标题",
        "messageCount": 5,
        "lastMessage": "最后一条消息内容",
        "lastMessageAt": "2023-01-01T10:00:00",
        "createdAt": "2023-01-01T00:00:00",
        "updatedAt": "2023-01-01T10:00:00"
      }
    ]
  }
  ```

### 3.3 获取会话详情
- **接口地址**：`/api/chat/sessions/{id}`
- **请求方法**：`GET`
- **请求头**：需要JWT认证
- **路径参数**：
  - `id`: 会话ID
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "会话标题",
      "messageCount": 5,
      "lastMessage": "最后一条消息内容",
      "lastMessageAt": "2023-01-01T10:00:00",
      "createdAt": "2023-01-01T00:00:00",
      "updatedAt": "2023-01-01T10:00:00"
    }
  }
  ```

### 3.4 删除会话
- **接口地址**：`/api/chat/sessions/{id}`
- **请求方法**：`DELETE`
- **请求头**：需要JWT认证
- **路径参数**：
  - `id`: 会话ID
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": true
  }
  ```

## 4. 聊天消息相关接口

### 4.1 发送消息
- **接口地址**：`/api/chat/messages`
- **请求方法**：`POST`
- **请求头**：需要JWT认证
- **请求体**：
  ```json
  {
    "sessionId": 1,
    "content": "消息内容"
  }
  ```
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": {
      "id": 1,
      "sessionId": 1,
      "role": "assistant",
      "content": "AI回复内容",
      "model": "gpt-3.5-turbo",
      "createdAt": "2023-01-01T10:00:00"
    }
  }
  ```

### 4.2 获取消息历史
- **接口地址**：`/api/chat/messages`
- **请求方法**：`GET`
- **请求头**：需要JWT认证
- **请求参数**：
  - `sessionId`: 会话ID
  - `page`: 页码，默认1
  - `size`: 每页数量，默认50
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": [
      {
        "id": 1,
        "sessionId": 1,
        "role": "user",
        "content": "用户消息内容",
        "model": null,
        "createdAt": "2023-01-01T10:00:00"
      },
      {
        "id": 2,
        "sessionId": 1,
        "role": "assistant",
        "content": "AI回复内容",
        "model": "gpt-3.5-turbo",
        "createdAt": "2023-01-01T10:00:01"
      }
    ]
  }
  ```

### 4.3 通过会话ID获取消息列表
- **接口地址**：`/api/chat/sessions/{sessionId}/messages`
- **请求方法**：`GET`
- **请求头**：需要JWT认证
- **路径参数**：
  - `sessionId`: 会话ID
- **请求参数**：
  - `page`: 页码，默认1
  - `size`: 每页数量，默认50
- **响应**：
  ```json
  {
    "code": 200,
    "message": "成功",
    "data": [
      {
        "id": 1,
        "sessionId": 1,
        "role": "user",
        "content": "用户消息内容",
        "model": null,
        "createdAt": "2023-01-01T10:00:00"
      },
      {
        "id": 2,
        "sessionId": 1,
        "role": "assistant",
        "content": "AI回复内容",
        "model": "gpt-3.5-turbo",
        "createdAt": "2023-01-01T10:00:01"
      }
    ]
  }
  ```
