import { defineStore } from 'pinia'
import * as chatApi from '../api/chat'
import { ElMessage } from 'element-plus'

export const useChatStore = defineStore('chat', {
  state: () => ({
    sessionList: [], // 会话列表
    currentSession: null, // 当前会话
    messageList: [], // 当前会话的消息列表
    isLoading: false, // 加载状态
    currentPage: 1, // 当前页码
    totalCount: 0 // 总消息数
  }),

  getters: {
    hasActiveSession: (state) => !!state.currentSession
  },

  actions: {
    // 创建新会话
    async createNewSession(title = '新会话') {
      try {
        this.isLoading = true
        const session = await chatApi.createSession({ title })
        this.sessionList.unshift(session)
        this.currentSession = session
        this.messageList = []
        this.currentPage = 1
        ElMessage.success('会话创建成功')
        return session
      } catch (error) {
        ElMessage.error('会话创建失败')
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 获取会话列表
    async loadSessionList() {
      try {
        this.isLoading = true
        this.sessionList = await chatApi.getSessionList({ page: 1, size: 100 })
        // 如果没有当前会话且有会话列表，默认选中第一个
        if (!this.currentSession && this.sessionList.length > 0) {
          this.currentSession = this.sessionList[0]
          await this.loadMessageList(this.currentSession.id)
        }
        return this.sessionList
      } catch (error) {
        ElMessage.error('加载会话列表失败')
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 切换会话
    async switchSession(sessionId) {
      const session = this.sessionList.find(s => s.id === sessionId)
      if (session) {
        this.currentSession = session
        this.messageList = []
        this.currentPage = 1
        await this.loadMessageList(sessionId)
        return session
      }
      return null
    },

    // 删除会话
    async deleteSession(sessionId) {
      try {
        this.isLoading = true
        await chatApi.deleteSession(sessionId)
        // 从会话列表中移除
        const index = this.sessionList.findIndex(s => s.id === sessionId)
        if (index > -1) {
          this.sessionList.splice(index, 1)
        }
        // 如果删除的是当前会话，重置当前会话和消息列表
        if (this.currentSession && this.currentSession.id === sessionId) {
          this.currentSession = null
          this.messageList = []
          // 如果还有会话，默认选中第一个
          if (this.sessionList.length > 0) {
            this.currentSession = this.sessionList[0]
            await this.loadMessageList(this.currentSession.id)
          }
        }
        ElMessage.success('会话删除成功')
        return true
      } catch (error) {
        ElMessage.error('会话删除失败')
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 加载消息列表
    async loadMessageList(sessionId, page = 1, size = 50) {
      try {
        this.isLoading = true
        const messages = await chatApi.getSessionMessages(sessionId, { page, size })
        if (page === 1) {
          this.messageList = messages.reverse() // 最新消息在下面
        } else {
          // 分页加载时，将新消息插入到前面
          this.messageList = [...messages.reverse(), ...this.messageList]
        }
        this.currentPage = page
        return this.messageList
      } catch (error) {
        ElMessage.error('加载消息失败')
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 发送消息
    async sendUserMessage(content) {
      if (!this.currentSession) {
        ElMessage.warning('请先创建或选择一个会话')
        return null
      }

      try {
        this.isLoading = true
        // 先在本地添加用户消息（乐观更新）
        const userMessage = {
          id: Date.now().toString(),
          content,
          role: 'user',
          createTime: new Date().toISOString(),
          isLoading: false
        }
        this.messageList.push(userMessage)
        
        // 添加AI正在输入的提示
        const aiTypingMessage = {
          id: 'typing-' + Date.now(),
          content: '',
          role: 'assistant',
          createTime: new Date().toISOString(),
          isLoading: true
        }
        this.messageList.push(aiTypingMessage)
        
        // 调用API发送消息
        const response = await chatApi.sendMessage({
          sessionId: this.currentSession.id,
          content
        })
        
        // 更新消息列表，移除临时消息，添加真实AI回复
        this.messageList = this.messageList.filter(msg => 
          msg.id !== userMessage.id && msg.id !== aiTypingMessage.id
        )
        this.messageList.push({
          id: response.id,
          content: response.content,
          role: response.role,
          createTime: response.createTime,
          isLoading: false
        })
        
        // 更新会话的最后一条消息
        if (this.currentSession) {
          this.currentSession.lastMessage = content
          this.currentSession.updateTime = response.createTime
          // 更新会话列表中的会话信息
          const index = this.sessionList.findIndex(s => s.id === this.currentSession.id)
          if (index > -1) {
            this.sessionList[index] = { ...this.currentSession }
          }
        }
        
        return response
      } catch (error) {
        ElMessage.error('发送消息失败')
        // 移除临时消息
        this.messageList = this.messageList.filter(msg => 
          msg.id !== 'typing-' + Date.now() && !msg.isLoading
        )
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 清空当前会话的消息列表
    clearCurrentMessages() {
      this.messageList = []
      this.currentPage = 1
    },

    // 重置聊天状态
    resetChatState() {
      this.sessionList = []
      this.currentSession = null
      this.messageList = []
      this.currentPage = 1
    }
  }
})