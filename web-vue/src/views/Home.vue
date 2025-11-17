<template>
  <div class="home-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用AI健康助手</span>
          <el-button type="danger" @click="handleLogout" size="small">登出</el-button>
        </div>
      </template>
      <div class="welcome-content">
        <h3>您好，{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</h3>
        <p class="welcome-text">这是您的用户首页，更多功能即将上线</p>
        <el-divider />
        <div class="user-info">
          <h4>用户信息</h4>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户名">{{ userStore.userInfo?.username }}</el-descriptions-item>
            <el-descriptions-item label="昵称">{{ userStore.userInfo?.nickname || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userStore.userInfo?.email || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ userStore.userInfo?.phone || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ userStore.userInfo?.role }}</el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ formatDate(userStore.userInfo?.createTime) }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getUserInfo } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

// 登出处理
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

// 页面加载时获取用户信息
onMounted(async () => {
  if (!userStore.userInfo) {
    try {
      // 由于http.js中的响应拦截器已经直接返回了data，不需要再访问.response.data
      const userData = await getUserInfo()
      userStore.setUserInfo(userData)
    } catch (error) {
      console.error('获取用户信息失败', error)
      // 如果失败，可以考虑清除登录状态并重定向到登录页
    }
  }
})
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-content {
  text-align: center;
}

.welcome-content h3 {
  color: #303133;
  margin-bottom: 10px;
}

.welcome-text {
  color: #606266;
  margin-bottom: 30px;
}

.user-info {
  margin-top: 30px;
}

.user-info h4 {
  text-align: left;
  margin-bottom: 20px;
  color: #303133;
}
</style>