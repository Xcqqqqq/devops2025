import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { login, register, getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)
  
  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  
  // 操作
  const setToken = (newToken) => {
    token.value = newToken
    if (newToken) {
      localStorage.setItem('token', newToken)
    } else {
      localStorage.removeItem('token')
    }
  }
  
  const setUserInfo = (info) => {
    userInfo.value = info
  }
  
  // 用户登录
  const userLogin = async (username, password) => {
    try {
      // 由于http.js的响应拦截器已经直接返回data对象，不需要再访问.response.data
      const loginData = await login({ username, password })
      
      // 保存token和用户信息
      setToken(loginData.token)
      setUserInfo(loginData.userInfo)
      
      ElMessage.success('登录成功')
      return true
    } catch (error) {
      // 在http.js中已经处理了错误消息，这里可以简化
      console.error('登录失败', error)
      return false
    }
  }
  
  // 用户注册
  const userRegister = async (username, password, nickname, email, phone) => {
    try {
      // 由于http.js的响应拦截器已经直接返回data对象
      const registerData = await register({ username, password, nickname, email, phone })
      
      // 根据后端返回的数据格式，可能需要调整消息获取方式
      ElMessage.success(registerData.message || '注册成功')
      return true
    } catch (error) {
      // 在http.js中已经处理了错误消息，这里可以简化
      console.error('注册失败', error)
      return false
    }
  }
  
  // 用户登出
  const logout = () => {
    setToken('')
    setUserInfo(null)
    ElMessage.success('登出成功')
  }
  
  // 初始化用户信息
  const initUserInfo = async () => {
    if (token.value) {
      try {
        const data = await getUserInfo()
        setUserInfo(data)
      } catch (error) {
        // 如果获取用户信息失败，清除token
        console.error('初始化用户信息失败', error)
        setToken('')
      }
    }
  }
  
  return {
    token,
    userInfo,
    isLoggedIn,
    userLogin,
    userRegister,
    logout,
    initUserInfo
  }
})