# 前端页面设计（替换用户欢迎页）：
- 左侧是会话列表区域，显示用户的所有会话，每个会话栏显示会话标题，最右侧有删除会话按钮
- 顶部是导航栏，包括标题”AI健康助手“和退出登录按钮
- 中间是聊天消息显示区域，显示历史对话的消息
  包含消息气泡功能，根据角色(user/assistant)展示不同样式的消息气泡
- 下方是消息输入区域，包含文本输入框和发送按钮

# 前端交互设计
1. 初始化会话 ：
   - 页面加载时自动创建新会话：调用 createSession API
   - 获取会话ID后，加载历史消息：调用 getMessages API
   - 如果没有历史消息，显示欢迎语
2. 发送消息 ：
   - 输入内容后点击发送按钮或按Enter键
   - 立即在本地添加用户消息到消息列表
   - 调用 sendMessage API向后端发送请求
   - 接收AI回复后添加到消息列表
   - 自动滚动到底部显示最新消息
3. 会话管理 ：
   - 点击左侧的会话列表项，加载该会话的历史消息
   - 点击会话栏的删除按钮，删除该会话
   - 点击返回按钮，返回会话列表页
4. 用户管理：
   - 点击退出登录按钮，清除本地token并返回登录页

# 前端数据流向
- 前端输入 → 后端接口 ：
  - 用户输入消息 → /chat/messages 接口
  - 新会话创建请求 → /chat/sessions 接口
  - 获取历史消息 → /chat/sessions/{sessionId}/messages 接口
- 后端接口 → 前端显示 ：
  - AI回复消息 → 显示在聊天区域
  - 会话列表 → 显示在个人中心的「我的对话记录」
  - 会话详情 → 加载历史聊天记录

# 后端接口设计
后端通过 ChatController 提供以下RESTful接口：
1. 创建会话 ：
   - POST /api/chat/sessions
   - 参数： CreateSessionDTO 
   - 返回： ChatSessionVO
2. 获取会话列表 ：
   - GET /api/chat/sessions
   - 参数： page （页码）、 size （每页数量）
   - 返回： List<ChatSessionVO>
3. 获取会话详情 ：
   - GET /api/chat/sessions/{id}
   - 参数： id （会话ID）
   - 返回： ChatSessionVO
4. 删除会话 ：
   - DELETE /api/chat/sessions/{id}
   - 参数： id （会话ID）
   - 返回： Boolean
5. 发送消息 ：
   - POST /api/chat/messages
   - 参数： SendMessageDTO （包含sessionId和content）
   - 返回： ChatMessageVO （AI回复消息）
6. 获取消息历史 ：
   - GET /api/chat/messages
   - 参数： sessionId （会话ID）、 page （页码）、 size （每页数量）
   - 返回： List<ChatMessageVO>

# 后端数据结构
1. ChatSession:
    - Long id
    - Long userId
    - String title
    - String type(会话类型，默认值为"chat"，目前只实现该类型)
    - Integer status(0 已结束 1 进行中)
    - Integer messageCount(会话中的消息数量)
    - LocalDateTime createAt(会话创建时间)
    - LocalDateTime updateAt(会话更新时间)

2. ChatMessage:
    - Long id
    - Long sessionId(会话ID)
    - String role(消息角色，"user"或"assistant")
    - String content(消息内容)
    - String model(模型名称)

# 数据库储存设计
1. chat_session（聊天会话表） ：
   - 存储用户的会话信息
   - 主要字段：id, user_id, title, type, status, last_message_time, created_at, updated_at, is_deleted
   - 索引：user_id（加速查询用户会话）

2. chat_message（聊天消息表） ：
   - 存储会话中的所有消息
   - 主要字段：id, session_id, role, content, model, created_at
   - 索引：session_id（加速查询会话消息）
