<template>
  <div class="register-container">
    <div class="register-form">
      <h2>个人智能体平台 - 注册</h2>
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="registerForm.nickname" placeholder="请输入昵称（可选）" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading">
            注册
          </el-button>
          <el-button @click="goLogin">已有账号？去登录</el-button>
        </el-form-item>
      </el-form>
      <div v-if="errorMsg" class="error-message">
        {{ errorMsg }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/userStore'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const registerFormRef = ref(null)
const loading = ref(false)
const errorMsg = ref('')

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  nickname: [
    { max: 50, message: '昵称长度不能超过 50 个字符', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  // 验证表单
  const valid = await registerFormRef.value.validate()
  if (!valid) return

  loading.value = true
  errorMsg.value = ''

  try {
    // userRegister方法直接返回布尔值，不是带success属性的对象
    const success = await userStore.register(
      registerForm.value.username,
      registerForm.value.password,
      registerForm.value.nickname
    )
    if (success) {
      // 注册成功后跳转到登录页
      // 注意：register方法内部已经处理了成功提示
      router.push('/login')
    } else {
      errorMsg.value = '注册失败，请稍后重试'
    }
  } catch (error) {
    console.error('注册错误:', error)
    errorMsg.value = '注册失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const goLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.register-form {
  width: 400px;
  padding: 30px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-form h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.error-message {
  color: #f56c6c;
  font-size: 14px;
  margin-top: 10px;
  text-align: center;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>