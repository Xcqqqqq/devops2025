import http from '@/utils/http'

// 创建会话
export const createSession = (data) => {
  return http({
    url: '/chat/sessions',
    method: 'post',
    data
  })
}

// 获取会话列表
export const getSessionList = (params) => {
  return http({
    url: '/chat/sessions',
    method: 'get',
    params
  })
}

// 获取会话详情
export const getSessionDetail = (id) => {
  return http({
    url: `/chat/sessions/${id}`,
    method: 'get'
  })
}

// 删除会话
export const deleteSession = (id) => {
  return http({
    url: `/chat/sessions/${id}`,
    method: 'delete'
  })
}

// 发送消息
export const sendMessage = (data) => {
  return http({
    url: '/chat/messages',
    method: 'post',
    data
  })
}

// 获取消息历史
export const getMessageHistory = (params) => {
  return http({
    url: '/chat/messages',
    method: 'get',
    params
  })
}

// 获取会话的消息列表（另一种路径方式）
export const getSessionMessages = (sessionId, params) => {
  return http({
    url: `/chat/sessions/${sessionId}/messages`,
    method: 'get',
    params
  })
}