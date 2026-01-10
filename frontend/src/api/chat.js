import { http } from '@/utils/http'

// 创建会话
export const createSession = (data) => {
  return http.post('/chat/sessions', data)
}

// 获取会话列表
export const getSessionList = (params) => {
  return http.get('/chat/sessions', { params })
}

// 获取会话详情
export const getSessionDetail = (id) => {
  return http.get(`/chat/sessions/${id}`)
}

// 删除会话
export const deleteSession = (id) => {
  return http.delete(`/chat/sessions/${id}`)
}

// 发送消息
export const sendMessage = (data) => {
  return http.post('/chat/messages', data)
}

// 获取消息历史
export const getMessageHistory = (params) => {
  return http.get('/chat/messages', { params })
}

// 获取会话的消息列表（另一种路径方式）
export const getSessionMessages = (sessionId, params) => {
  return http.get(`/chat/sessions/${sessionId}/messages`, { params })
}