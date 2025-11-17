import { defineStore } from 'pinia'
import axios from '../utils/axiosConfig'

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
    initUserInfo() {
      const token = localStorage.getItem('token')
      const userInfoStr = localStorage.getItem('userInfo')
      if (token && userInfoStr) {
        this.token = token
        this.userInfo = JSON.parse(userInfoStr)
      }
    },

    // 用户登录
    async login(username, password) {
      try {
        const response = await axios.post('/api/user/login', {
          username,
          password
        })
        if (response.data.code === 200) {
          const { data } = response.data
          this.setUserInfo(data, data.token)
          return { success: true }
        } else {
          return { success: false, message: response.data.message }
        }
      } catch (error) {
        return { success: false, message: error.message || '登录失败' }
      }
    },

    // 用户注册
    async register(username, password, nickname = '', email = '', phone = '') {
      try {
        const response = await axios.post('/api/user/register', {
          username,
          password,
          nickname,
          email,
          phone
        })
        if (response.data.code === 200) {
          return { success: true }
        } else {
          return { success: false, message: response.data.message }
        }
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