import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './style.css'

// 创建Vue应用实例
const app = createApp(App)

// 注册Element Plus
app.use(ElementPlus)

// 注册Pinia
app.use(createPinia())

// 注册路由
app.use(router)

// 挂载应用
app.mount('#app')
