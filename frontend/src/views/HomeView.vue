<template>
  <div class="home-container">
    <!-- 顶部栏 -->
    <div class="top-header">
      <div class="user-info">
        <el-avatar :size="40" :src="userInfo?.avatar">{{ userInfo?.nickname?.[0] || userInfo?.username?.[0] }}</el-avatar>
        <div class="user-details">
          <span class="username">{{ userInfo?.nickname || userInfo?.username }}</span>
          <span class="user-permission" v-if="userPermission">{{ userPermission }}</span>
        </div>
      </div>
      <el-button type="primary" @click="handleLogout">退出登录</el-button>
    </div>
    
    <!-- 主体内容 -->
    <div class="main-content">
      <!-- 左侧导航栏 -->
      <div class="sidebar">
        <el-menu default-active="1" class="sidebar-menu">
          <el-menu-item index="1">
            <template #title>
              <span>主页</span>
            </template>
          </el-menu-item>
          <el-menu-item index="2">
            <template #title>
              <span>用户信息</span>
            </template>
          </el-menu-item>
        </el-menu>
      </div>
      
      <!-- 中间内容区 -->
      <div class="content-area">
        <h2>智能体列表</h2>
        <div class="agent-list">
          <!-- 智能体卡片 -->
          <div class="agent-card" v-for="agent in agentList" :key="agent.id">
            <div class="agent-avatar">
              <el-icon class="avatar-icon"><Avatar /></el-icon>
            </div>
            <div class="agent-info">
              <h3 class="agent-name">{{ agent.name }}</h3>
              <p class="agent-description">{{ agent.description }}</p>
            </div>
            <div class="agent-actions">
              <el-button type="primary" size="small" @click="handleEditAgent(agent)">
                <el-icon><Edit /></el-icon> 编辑
              </el-button>
              <el-button type="danger" size="small" @click="handleDeleteAgent(agent)">
                <el-icon><Delete /></el-icon> 删除
              </el-button>
            </div>
          </div>
          
          <!-- 添加智能体按钮 -->
          <div class="add-agent-card" @click="handleAddAgent">
            <div class="add-agent-icon">
              <el-icon class="plus-icon"><Plus /></el-icon>
            </div>
            <span>添加智能体</span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 添加智能体弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="添加智能体"
      width="500px"
      @close="resetAddForm"
    >
      <el-form :model="addAgentForm" label-width="80px">
        <el-form-item label="名称" required>
          <el-input v-model="addAgentForm.name" placeholder="请输入智能体名称" />
        </el-form-item>
        <el-form-item label="描述" required>
          <el-input v-model="addAgentForm.description" type="textarea" placeholder="请输入智能体描述" :rows="3" />
        </el-form-item>
        <el-form-item label="提示词" required>
          <el-input v-model="addAgentForm.prompt" type="textarea" placeholder="请输入智能体提示词" :rows="5" />
        </el-form-item>
        <el-form-item v-if="userInfo?.permission === 1" label="是否公开">
          <el-select v-model="addAgentForm.isPublic" placeholder="请选择">
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleCreateAgent">确认</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 编辑智能体弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑智能体"
      width="500px"
      @close="resetEditForm"
    >
      <el-form :model="editAgentForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="editAgentForm.name" placeholder="请输入智能体名称" disabled />
        </el-form-item>
        <el-form-item label="描述" required>
          <el-input v-model="editAgentForm.description" type="textarea" placeholder="请输入智能体描述" :rows="3" />
        </el-form-item>
        <el-form-item label="提示词" required>
          <el-input v-model="editAgentForm.prompt" type="textarea" placeholder="请输入智能体提示词" :rows="5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateAgent">确认</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 删除确认弹窗 -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="确认删除"
      width="400px"
    >
      <span>您确定要删除该智能体吗？此操作不可恢复。</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="handleConfirmDelete">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/userStore'
import { ElMessage } from 'element-plus'
// 导入Element Plus图标
import { Avatar, Edit, Delete, Plus } from '@element-plus/icons-vue'
// 导入agent API
import { getAgentList, createAgent, updateAgent, deleteAgent } from '../api/agent'

const router = useRouter()
const userStore = useUserStore()

// 计算属性获取用户信息
const userInfo = computed(() => userStore.userInfo)

// 计算属性获取用户权限名称
const userPermission = computed(() => {
  if (!userInfo.value?.permission) return ''
  return userInfo.value.permission === 1 ? '管理员' : '普通用户'
})

// 智能体列表
const agentList = ref([])

// 弹窗状态
const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const deleteDialogVisible = ref(false)

// 表单数据
const addAgentForm = ref({
  name: '',
  description: '',
  prompt: '',
  isPublic: false
})

const editAgentForm = ref({
  id: null,
  name: '',
  description: '',
  prompt: ''
})

// 待删除的智能体ID
const deleteAgentId = ref(null)

// 组件挂载时初始化
onMounted(async () => {
  await userStore.initUserInfo()
  // 如果用户未登录，重定向到登录页
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  // 获取智能体列表
  getAgentListData()
})

// 获取智能体列表数据
const getAgentListData = async () => {
  try {
    // console.log('getAgentListData called, userInfo:', userInfo.value)
    if (!userInfo.value?.id) {
      console.log('userInfo.id is null, cannot get agent list')
      return
    }
    console.log('Calling getAgentList')
    const data = await getAgentList()
    console.log('Agent list received:', data)
    agentList.value = data
  } catch (error) {
    console.error('获取智能体列表失败:', error)
    ElMessage.error('获取智能体列表失败')
  }
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出成功')
  router.push('/login')
}

// 打开添加智能体弹窗
const handleAddAgent = () => {
  addDialogVisible.value = true
}

// 重置添加表单
const resetAddForm = () => {
  addAgentForm.value = {
    name: '',
    description: '',
    prompt: '',
    isPublic: false
  }
}

// 创建智能体
const handleCreateAgent = async () => {
  try {
    // 表单验证
    if (!addAgentForm.value.name || !addAgentForm.value.description || !addAgentForm.value.prompt) {
      ElMessage.warning('请填写完整的智能体信息')
      return
    }
    
    // 准备请求参数
    const params = {
      ...addAgentForm.value
    }
    
    // 如果用户不是管理员，移除public字段
    if (userInfo.value?.permission !== 1) {
      delete params.isPublic
    }
    
    // 调用API创建智能体
    await createAgent(params)
    ElMessage.success('智能体创建成功')
    
    // 关闭弹窗并刷新列表
    addDialogVisible.value = false
    resetAddForm()
    getAgentListData()
  } catch (error) {
    console.error('创建智能体失败:', error)
    ElMessage.error('创建智能体失败')
  }
}

// 打开编辑智能体弹窗
const handleEditAgent = (agent) => {
  editAgentForm.value = {
    id: agent.id,
    name: agent.name,
    description: agent.description,
    prompt: agent.prompt
  }
  editDialogVisible.value = true
}

// 重置编辑表单
const resetEditForm = () => {
  editAgentForm.value = {
    id: null,
    name: '',
    description: '',
    prompt: ''
  }
}

// 更新智能体
const handleUpdateAgent = async () => {
  try {
    // 表单验证
    if (!editAgentForm.value.description || !editAgentForm.value.prompt) {
      ElMessage.warning('请填写完整的智能体信息')
      return
    }
    
    // 准备请求参数
    const params = {
      description: editAgentForm.value.description,
      prompt: editAgentForm.value.prompt
    }
    
    // 调用API更新智能体
    await updateAgent(editAgentForm.value.id, params)
    ElMessage.success('智能体更新成功')
    
    // 关闭弹窗并刷新列表
    editDialogVisible.value = false
    resetEditForm()
    getAgentListData()
  } catch (error) {
    console.error('更新智能体失败:', error)
    ElMessage.error('更新智能体失败')
  }
}

// 打开删除智能体弹窗
const handleDeleteAgent = (agent) => {
  deleteAgentId.value = agent.id
  deleteDialogVisible.value = true
}

// 确认删除智能体
const handleConfirmDelete = async () => {
  try {
    if (!deleteAgentId.value) return
    
    // 调用API删除智能体
    await deleteAgent(deleteAgentId.value)
    ElMessage.success('智能体删除成功')
    
    // 关闭弹窗并刷新列表
    deleteDialogVisible.value = false
    deleteAgentId.value = null
    getAgentListData()
  } catch (error) {
    console.error('删除智能体失败:', error)
    ElMessage.error('删除智能体失败')
  }
}
</script>

<style scoped>
.home-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

/* 顶部栏样式 */
.top-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 2px;
}

.username {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.user-permission {
  font-size: 12px;
  color: #606266;
  background-color: #ecf5ff;
  padding: 2px 8px;
  border-radius: 10px;
  width: fit-content;
}

/* 主体内容样式 */
.main-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 左侧导航栏样式 */
.sidebar {
  width: 200px;
  background-color: #ffffff;
  border-right: 1px solid #eaeaea;
}

.sidebar-menu {
  border-right: none;
  height: 100%;
}

/* 中间内容区样式 */
.content-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.content-area h2 {
  margin-bottom: 20px;
  color: #303133;
}

/* 智能体列表样式 */
.agent-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

/* 智能体卡片样式 */
.agent-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: box-shadow 0.3s ease;
}

.agent-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

/* 智能体头像样式 */
.agent-avatar {
  width: 80px;
  height: 80px;
  background-color: #409eff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16px;
}

.avatar-icon {
  font-size: 40px;
  color: #ffffff;
}

/* 智能体信息样式 */
.agent-info {
  text-align: center;
  margin-bottom: 20px;
  width: 100%;
}

.agent-name {
  margin: 0 0 8px 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.agent-description {
  margin: 0;
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 智能体操作按钮样式 */
.agent-actions {
  display: flex;
  gap: 10px;
  width: 100%;
  justify-content: center;
}

/* 添加智能体卡片样式 */
.add-agent-card {
  background-color: #ffffff;
  border-radius: 8px;
  border: 2px dashed #dcdfe6;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  height: 100%;
}

.add-agent-card:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.add-agent-icon {
  width: 80px;
  height: 80px;
  background-color: #ecf5ff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16px;
}

.plus-icon {
  font-size: 40px;
  color: #409eff;
}

.add-agent-card span {
  color: #409eff;
  font-size: 16px;
  font-weight: 500;
}
</style>