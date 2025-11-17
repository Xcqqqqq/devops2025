<template>
  <div class="home-container">
    <div class="header">
      <h1>欢迎回来，{{ userInfo?.nickname || userInfo?.username }}</h1>
      <el-button type="primary" @click="handleLogout">退出登录</el-button>
    </div>
    <div class="content">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>AI健康助手</span>
          </div>
        </template>
        <div class="card-content">
          <p>这是用户首页，您可以在这里进行健康咨询和管理个人健康档案。</p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/userStore'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

// 计算属性获取用户信息
const userInfo = computed(() => userStore.userInfo)

// 组件挂载时初始化用户信息
onMounted(() => {
  userStore.initUserInfo()
  // 如果用户未登录，重定向到登录页
  if (!userStore.isLoggedIn) {
    router.push('/login')
  }
})

// 退出登录
const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px 0;
  border-bottom: 1px solid #eaeaea;
}

.header h1 {
  margin: 0;
  color: #303133;
}

.content {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  padding: 20px 0;
  font-size: 16px;
  line-height: 1.8;
}
</style>