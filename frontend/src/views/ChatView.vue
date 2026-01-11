<template>
  <div class="chat-container">
    <!-- 左侧会话列表 -->
    <div class="session-list">
      <div class="session-header">
        <el-button type="default" size="small" @click="handleBackToHome">
          <el-icon><ArrowLeft /></el-icon> 返回主页
        </el-button>
        <h2>{{ agentInfo?.name || 'AI助手' }}</h2>
        <el-button type="primary" size="small" @click="handleNewSession">
          <el-icon><Plus /></el-icon> 新建会话
        </el-button>
      </div>
      <div class="session-items">
        <div
          v-for="session in chatStore.sessionList"
          :key="session.id"
          class="session-item"
          :class="{ active: chatStore.currentSession?.id === session.id }"
          @click="handleSwitchSession(session.id)"
        >
          <div class="session-info">
            <h3>{{ session.title }}</h3>
            <p class="last-message">{{ session.lastMessage || '暂无消息' }}</p>
          </div>
          <div class="session-actions">
            <el-button
              type="text"
              size="small"
              @click.stop="handleDeleteSession(session.id)"
              title="删除会话"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧聊天窗口 -->
    <div class="chat-window">
      <div v-if="chatStore.hasActiveSession" class="chat-content">
        <!-- 聊天头部 -->
        <div class="chat-header">
          <h2>{{ chatStore.currentSession.title }}</h2>
          <el-button type="text" @click="handleRenameSession" title="重命名会话">
            <el-icon><Edit /></el-icon> 重命名
          </el-button>
        </div>

        <!-- 消息列表 -->
        <div class="message-list" ref="messageListRef">
          <div v-if="chatStore.isLoading && chatStore.messageList.length === 0" class="loading">
            <el-skeleton :rows="3" animated />
          </div>
          <div v-else-if="chatStore.messageList.length === 0" class="empty-messages">
            <el-empty description="开始与AI助手对话吧" />
          </div>
          <div
            v-for="message in chatStore.messageList"
            :key="message.id"
            class="message-item"
            :class="message.role"
          >
            <div class="message-avatar">
              <img v-if="message.role === 'user'" src="@/assets/user-avatar.svg" alt="用户" />
              <img v-else src="@/assets/ai-avatar.svg" alt="AI" />
            </div>
            <div class="message-content">
              <div v-if="message.isLoading" class="typing">
                <el-skeleton :rows="2" animated />
              </div>
              <div v-else class="message-text">{{ message.content }}</div>
            </div>
          </div>
        </div>

        <!-- 输入框 -->
        <div class="chat-input">
          <el-input
            v-model="inputContent"
            type="textarea"
            placeholder="请输入消息..."
            :rows="3"
            resize="none"
            @keyup.enter.ctrl="handleSendMessage"
            @keyup.enter.meta="handleSendMessage"
          />
          <div class="input-actions">
            <span class="tip">Ctrl+Enter 发送</span>
            <el-button type="primary" @click="handleSendMessage">发送</el-button>
          </div>
        </div>
      </div>
      <div v-else class="no-session">
        <el-empty description="请创建或选择一个会话开始聊天" />
        <el-button type="primary" @click="handleNewSession">新建会话</el-button>
      </div>
    </div>

    <!-- 重命名会话对话框 -->
    <el-dialog
      v-model="renameDialogVisible"
      title="重命名会话"
      width="400px"
    >
      <el-input
        v-model="newSessionTitle"
        placeholder="请输入新的会话标题"
        maxlength="50"
        show-word-limit
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeRenameDialog">取消</el-button>
          <el-button type="primary" @click="confirmRenameSession">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新建会话对话框 -->
    <el-dialog
      v-model="newSessionDialogVisible"
      title="新建会话"
      width="400px"
    >
      <el-input
        v-model="newSessionName"
        placeholder="请输入会话名称"
        maxlength="50"
        show-word-limit
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="newSessionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmCreateSession">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import { Plus, Delete, Edit, ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useChatStore } from '../stores/chatStore'
import { useUserStore } from '../stores/userStore'
import { useRouter, useRoute } from 'vue-router'
import { getAgentById } from '../api/agent'

const chatStore = useChatStore()
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const inputContent = ref('')
const messageListRef = ref(null)
const renameDialogVisible = ref(false)
const newSessionTitle = ref('')
const agentInfo = ref(null)
// 新建会话对话框相关
const newSessionDialogVisible = ref(false)
const newSessionName = ref('')

// 监听消息列表变化，自动滚动到底部
watch(
  () => chatStore.messageList.length,
  async () => {
    await nextTick()
    scrollToBottom()
  }
)

// 滚动到底部
const scrollToBottom = () => {
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}

// 返回主页
const handleBackToHome = () => {
  router.push('/home')
}

// 创建新会话（显示对话框）
const handleNewSession = () => {
  newSessionName.value = '新会话'
  newSessionDialogVisible.value = true
}

// 确认创建会话
const confirmCreateSession = async () => {
  if (!newSessionName.value.trim()) {
    ElMessage.warning('请输入会话名称')
    return
  }
  
  try {
    const agentId = route.params.agentId
    await chatStore.createNewSession(newSessionName.value.trim(), agentId)
    newSessionDialogVisible.value = false
  } catch (error) {
    console.error('创建会话失败:', error)
  }
}

// 切换会话
const handleSwitchSession = async (sessionId) => {
  try {
    await chatStore.switchSession(sessionId)
  } catch (error) {
    console.error('切换会话失败:', error)
  }
}

// 删除会话
const handleDeleteSession = async (sessionId) => {
  try {
    await ElMessageBox.confirm('确定要删除这个会话吗？删除后将无法恢复。', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await chatStore.deleteSession(sessionId)
  } catch (error) {
    // 用户取消删除
    if (error !== 'cancel') {
      console.error('删除会话失败:', error)
    }
  }
}

// 发送消息
const handleSendMessage = async () => {
  const content = inputContent.value.trim()
  if (!content) {
    ElMessage.warning('请输入消息内容')
    return
  }

  try {
    inputContent.value = ''
    await chatStore.sendUserMessage(content)
  } catch (error) {
    console.error('发送消息失败:', error)
  }
}

// 打开重命名对话框
const handleRenameSession = () => {
  if (chatStore.currentSession) {
    newSessionTitle.value = chatStore.currentSession.title
    renameDialogVisible.value = true
  }
}

// 确认重命名会话
const confirmRenameSession = async () => {
  const title = newSessionTitle.value.trim()
  if (!title) {
    ElMessage.warning('请输入会话标题')
    return
  }

  try {
    // 这里可以添加API调用更新会话标题
    // 暂时只在本地更新
    if (chatStore.currentSession) {
      chatStore.currentSession.title = title
      // 更新会话列表中的会话标题
      const session = chatStore.sessionList.find(s => s.id === chatStore.currentSession.id)
      if (session) {
        session.title = title
      }
    }
    renameDialogVisible.value = false
    ElMessage.success('会话标题更新成功')
  } catch (error) {
    console.error('重命名会话失败:', error)
    ElMessage.error('更新标题失败')
  }
}

// 关闭弹窗的方法
const closeRenameDialog = () => {
  renameDialogVisible.value = false
}

// 获取当前agent信息
const getAgentInfo = async () => {
  try {
    const agentId = route.params.agentId
    if (agentId) {
      const agent = await getAgentById(agentId)
      agentInfo.value = agent
    }
  } catch (error) {
    console.error('获取智能体信息失败:', error)
  }
}

// 组件挂载时初始化
onMounted(async () => {
  // 检查用户是否登录
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }

  try {
    // 获取当前agent信息
    await getAgentInfo()
    // 加载会话列表
    await chatStore.loadSessionList(route.params.agentId)
  } catch (error) {
    console.error('初始化聊天失败:', error)
  }
})
</script>

<style scoped>
.chat-container {
  display: flex;
  height: 100vh;
  background-color: #f5f7fa;
}

/* 左侧会话列表 */
.session-list {
  width: 300px;
  background-color: #ffffff;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
}

.session-header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.session-header h2 {
  flex: 1;
  text-align: center;
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.session-items {
  flex: 1;
  overflow-y: auto;
}

.session-item {
  padding: 15px 20px;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: background-color 0.3s;
}

.session-item:hover {
  background-color: #f5f7fa;
}

.session-item.active {
  background-color: #ecf5ff;
  border-right: 3px solid #409eff;
}

.session-info h3 {
  margin: 0 0 5px 0;
  font-size: 16px;
  color: #303133;
}

.last-message {
  margin: 0;
  font-size: 14px;
  color: #909399;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 200px;
}

/* 右侧聊天窗口 */
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.no-session {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.chat-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  background-color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-header h2 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.message-list {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}

.message-item {
  display: flex;
  margin-bottom: 20px;
  align-items: flex-start;
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.message-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-content {
  max-width: 70%;
  padding: 0 15px;
}

.message-item.user .message-content {
  text-align: right;
}

.message-text {
  background-color: #ffffff;
  padding: 12px 16px;
  border-radius: 8px;
  word-wrap: break-word;
  font-size: 15px;
  line-height: 1.5;
  color: #303133;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-item.user .message-text {
  background-color: #409eff;
  color: #ffffff;
}

.typing {
  width: 200px;
}

.chat-input {
  padding: 20px;
  background-color: #ffffff;
  border-top: 1px solid #e4e7ed;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.tip {
  color: #909399;
  font-size: 14px;
}

/* 滚动条样式 */
.session-items::-webkit-scrollbar,
.message-list::-webkit-scrollbar {
  width: 6px;
}

.session-items::-webkit-scrollbar-track,
.message-list::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.session-items::-webkit-scrollbar-thumb,
.message-list::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 3px;
}

.session-items::-webkit-scrollbar-thumb:hover,
.message-list::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
