# 用户登录、注册功能详解

## 1. 技术栈概览

### 1.1 后端技术
- **Spring Boot**: 提供Web应用框架
- **JWT (JSON Web Token)**: 用户身份认证和授权
- **Spring Security**: 密码加密和安全管理
- **MyBatis**: ORM框架，数据库操作
- **Swagger/OpenAPI**: API文档生成

### 1.2 前端技术
- **Vue.js**: 前端框架
- **Pinia**: 状态管理
- **Vant UI**: H5移动端UI组件库
- **Element Plus**: Web管理端UI组件库

## 2. 后端实现

### 2.1 用户实体设计
```java
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender; // 0-未知 1-男 2-女
    private LocalDate birthday;
    private Integer status; // 0-禁用 1-正常
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isDeleted;
}
```

### 2.2 用户注册流程
1. **接口定义**：`POST /api/user/register`
2. **实现步骤**：
   - 检查用户名是否已存在
   - 使用Spring Security的PasswordEncoder加密密码
   - 创建用户记录，设置默认状态为正常
   - 保存到数据库

### 2.3 用户登录流程
1. **接口定义**：`POST /api/user/login`
2. **实现步骤**：
   - 根据用户名查询用户
   - 验证用户存在性和状态
   - 验证密码正确性
   - 使用JWT生成身份令牌
   - 返回用户信息和令牌

### 2.4 JWT认证机制
- 使用`io.jsonwebtoken`库生成和解析JWT
- 令牌包含用户ID、用户名等信息
- 支持从请求头、查询参数等多种方式获取令牌
- 提供令牌验证和用户信息提取功能

## 3. 前端实现

### 3.1 API调用封装
- 使用统一的HTTP工具函数封装API调用
- 登录注册接口路径为`/user/login`和`/user/register`

### 3.2 状态管理
- 使用Pinia的useUserStore管理用户状态
- 存储用户信息和令牌
- 提供登录、登出、初始化用户等方法
- 令牌持久化到localStorage

### 3.3 登录页面实现
- H5移动端和Web管理端都有独立的登录页面
- 表单验证确保用户名和密码不为空
- 登录成功后保存令牌和用户信息到状态管理
- 跳转到首页或指定页面

## 4. 安全措施

### 4.1 密码安全
- 使用Spring Security的PasswordEncoder进行密码加密
- 登录时使用matches方法验证密码

### 4.2 令牌安全
- JWT使用密钥签名，防止伪造
- 令牌设置过期时间
- 支持从请求头的Authorization字段获取令牌

### 4.3 用户状态控制
- 用户账户可以被禁用（status=0）
- 登录时验证用户状态

## 5. 用户信息管理

### 5.1 获取当前用户信息
- 接口：`GET /api/user/info`
- 从JWT中提取用户ID
- 查询并返回用户信息

### 5.2 根据ID获取用户信息
- 接口：`GET /api/user/{id}`
- 用于管理员查询用户信息

## 6. 测试账号
系统提供测试账号：`testuser` / `admin123`

## 7. 功能流程图

### 7.1 注册流程
```
用户输入注册信息 → 前端表单验证 → 调用注册API → 后端检查用户名是否存在 → 密码加密 → 创建用户记录 → 返回注册结果
```

### 7.2 登录流程
```
用户输入凭证 → 前端表单验证 → 调用登录API → 后端查询用户 → 验证密码 → 生成JWT令牌 → 返回用户信息和令牌 → 前端保存状态 → 跳转页面
```

### 7.3 认证流程
```
请求需要认证的接口 → 前端添加JWT到请求头 → 后端解析JWT → 验证令牌有效性 → 提取用户信息 → 执行业务逻辑 → 返回结果
```

## 8. 代码结构

### 8.1 后端核心文件
- `UserController.java`: 处理用户相关HTTP请求
- `UserServiceImpl.java`: 实现用户业务逻辑
- `UserMapper.java`: 数据库操作接口
- `JwtUtil.java`: JWT工具类
- `User.java`: 用户实体类
- `UserRegisterDTO.java`: 注册数据传输对象
- `UserLoginDTO.java`: 登录数据传输对象
- `UserVO.java`: 用户视图对象

### 8.2 前端核心文件
- `user.js`: API调用封装
- `user.js` (store): 用户状态管理
- `Login.vue` (H5): 移动端登录页面组件
- `LoginView.vue` (Web): 管理端登录页面组件
- `UserCenter.vue`: 用户中心页面

