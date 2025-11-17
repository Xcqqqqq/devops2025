import http from '@/utils/http'

// 用户登录
export const login = (data) => {
  return http({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 用户注册
export const register = (data) => {
  return http({
    url: '/user/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export const getUserInfo = () => {
  return http({
    url: '/user/info',
    method: 'get'
  })
}