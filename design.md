# 总体设计：AI健康助手
功能：交互式医疗问答 + 个人健康档案
分模块实现：
前端(web-vue/)：
- 前端登录/注册页面
- 用户欢迎页（聊天框）
- 用户个人页（健康档案）
- VIP用户专有页面（定制智能体）

后端(api-backend/)：
- 用户注册/登录接口
    注册 /api/user/register 信息填入数据库
    登录 /api/user/login 比对数据库 + 返回token
- 聊天消息接口
- 个人档案接口
- VIP用户接口

数据库（database/）：
- MyBatis 用于数据库操作

# 后端技术栈
backend:
  framework: Spring Boot
  database: MySQL 8.0 + MyBatis
  cache: Redis
  security: Spring Security + JWT
  api-docs: Swagger/OpenAPI 3.0

# 前端技术栈  
frontend:
  framework: Vue.js 3 + Composition API
  build-tool: Vite
  ui-library: Element Plus
  state-management: Pinia
  http-client: Axios

# AI技术栈
ai-service:
  llm-provider: Zhipu GLM / Qwen / Baichuan / SparkDesk / DeepSeek
  embedding: bge-m3 / qwen-embed / glm-embed / ernie-embedding / deepseek-embedding
  vector-db: Pinecone / Weaviate
  rag-framework: LangChain

# 向量数据库（Python实现）
python_vector_db:
  runtime: Python 3.10+
  libraries:
    - langchain
    - faiss-cpu
    - sentence-transformers
    - chromadb  # 可选
    - pymilvus  # 可选
  embeddings:
    - openai text-embedding-3-small
    - bge-m3  # 可选
  persistence:
    local_index: FAISS on disk ('kb/')
    remote_index: Milvus / Weaviate / Pinecone
  tooling:
    - unstructured  # 文档解析
    - requests      # 联调测试

