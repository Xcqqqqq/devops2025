import { http } from '@/utils/http'

// 用户登录
export const login = (data) => {
  return http.post('/user/login', data)
}

// 用户注册
export const register = (data) => {
  return http.post('/user/register', data)
}

// 获取用户信息
export const getUserInfo = () => {
  return http.get('/user/info')
}