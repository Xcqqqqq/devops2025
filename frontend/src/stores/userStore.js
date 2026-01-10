import { defineStore } from 'pinia'
import { http } from '../utils/http'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null,
    token: localStorage.getItem('token') || null
  }),

  getters: {
    isLoggedIn: (state) => !!state.token
  },

  actions: {
    // 保存用户信息和token
    setUserInfo(userInfo, token) {
      this.userInfo = userInfo
      this.token = token
      localStorage.setItem('token', token)
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },

    // 清除用户信息和token
    clearUserInfo() {
      this.userInfo = null
      this.token = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    },

    // 从localStorage初始化用户信息
    async initUserInfo() {
      const token = localStorage.getItem('token')
      if (token) {
        this.token = token
        try {
          // 尝试从localStorage获取用户信息
          const userInfoStr = localStorage.getItem('userInfo')
          if (userInfoStr) {
            this.userInfo = JSON.parse(userInfoStr)
          } else {
            // 如果localStorage中没有用户信息，调用接口获取
            const userInfo = await http.get('/user/info')
            this.userInfo = userInfo
            localStorage.setItem('userInfo', JSON.stringify(userInfo))
          }
        } catch (error) {
          console.error('获取用户信息失败:', error)
          // 如果获取失败，清除token和用户信息
          this.clearUserInfo()
        }
      }
    },

    // 用户登录
    async login(username, password) {
      try {
        const response = await http.post('/user/login', {
          username,
          password
        })
        // http.js的响应拦截器已经处理了统一的响应格式
        // 当响应的code为0时，它会直接返回payload.data
        // response的结构是 { token: 'xxx', userInfo: { ... } }
        this.setUserInfo(response.userInfo, response.token)
        return { success: true }
      } catch (error) {
        return { success: false, message: error.message || '登录失败' }
      }
    },

    // 用户注册
    async register(username, password, nickname = '') {
      try {
        const response = await http.post('/user/register', {
          username,
          password,
          nickname
        })
        // http.js的响应拦截器已经处理了统一的响应格式
        // 当响应的code为0时，它会直接返回payload.data
        return { success: true }
      } catch (error) {
        return { success: false, message: error.message || '注册失败' }
      }
    },

    // 用户登出
    logout() {
      this.clearUserInfo()
    }
  }
})