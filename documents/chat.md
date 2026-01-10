聊天页面：ChatView 展示与指定智能体的对话界面

左侧：会话列表
- 功能：展示当前用户与当前智能体的所有会话
- 按钮：返回主页
- 按钮：新建会话（弹窗输入会话名称）

右侧：聊天窗口
- 功能：展示当前会话的消息历史
- 输入框：发送消息（支持Ctrl+Enter发送）
- 按钮：发送消息


接口：`POST /api/chat/sessions`
行为：创建新的聊天会话
输入：
    Long agentId
    String title
行为：创建会话对象保存到后端数据库chat_session表中，关联用户和智能体
返回：创建的会话对象


接口：`GET /api/chat/sessions`
行为：获取当前用户的会话列表（分页）
输入：
    Integer page (可选，默认1)
    Integer size (可选，默认20)
行为：按当前userId筛选数据库chat_session表中的对象，分页返回
返回：会话对象列表


接口：`GET /api/chat/sessions/{id}`
行为：获取指定会话的详情
输入：会话ID
行为：按ID搜索数据库chat_session表中的对象，验证用户权限后返回
返回：会话对象
异常：会话不存在或无权访问


接口：`DELETE /api/chat/sessions/{id}`
行为：删除指定会话
输入：会话ID
行为：验证用户权限后删除数据库chat_session表中的会话对象及其所有消息
返回：删除成功状态
异常：会话不存在或无权访问


接口：`POST /api/chat/messages`
行为：发送消息并获取AI回复
输入：
    Long sessionId
    String content
行为：
    1. 验证会话权限
    2. 保存用户消息到chat_message表
    3. 调用模型API获取AI回复
    4. 保存AI回复到chat_message表
    5. 更新会话的消息数量和更新时间
返回：AI回复消息对象


接口：`GET /api/chat/messages`
行为：获取指定会话的消息历史（分页）
输入：
    Long sessionId
    Integer page (可选，默认1)
    Integer size (可选，默认50)
行为：按会话ID筛选数据库chat_message表中的消息，验证用户权限后分页返回
返回：消息对象列表


接口：`GET /api/chat/sessions/{sessionId}/messages`
行为：获取指定会话的消息历史（分页，与上面接口功能相同）
输入：
    Long sessionId
    Integer page (可选，默认1)
    Integer size (可选，默认50)
行为：按会话ID筛选数据库chat_message表中的消息，验证用户权限后分页返回
返回：消息对象列表
