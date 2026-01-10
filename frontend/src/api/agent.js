import { http } from '@/utils/http'

// 获取用户智能体列表
export const getAgentList = () => {
  return http.get('/agent/list')
}

// 创建智能体
export const createAgent = (data) => {
  return http.post('/agent/create', data)
}

// 更新智能体
export const updateAgent = (id, data) => {
  return http.put(`/agent/update/${id}`, data)
}

// 删除智能体
export const deleteAgent = (id) => {
  return http.delete(`/agent/delete/${id}`)
}