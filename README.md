# EasyStaff 员工管理系统

EasyStaff 是一个基于 Spring Boot 和 Vue 3 的前后端分离的员工管理系统，提供了用户认证和员工信息管理的完整功能。

## 📋 项目简介

EasyStaff 是一个现代化的员工管理系统，采用前后端分离架构，后端使用 Spring Boot 4.0 + MyBatis，前端使用 Vue 3 + TypeScript + Element Plus。系统提供了用户登录认证、员工信息的增删改查、分页查询等功能。

## 🛠️ 技术栈

### 后端技术

- **框架**: Spring Boot 4.0.0
- **语言**: Java 21
- **ORM**: MyBatis 3.0.3
- **数据库**: MySQL
- **构建工具**: Maven
- **其他**: Lombok, Spring Validation

### 前端技术

- **框架**: Vue 3.5.24
- **语言**: TypeScript 5.9.3
- **构建工具**: Vite 7.2.4
- **UI 组件库**: Element Plus 2.11.9
- **路由**: Vue Router 4.6.3
- **HTTP 客户端**: Axios 1.13.2
- **代码规范**: ESLint + Prettier

## 📁 项目结构

```
EasyStaff/
├── src/                          # 后端源代码
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/easystaff/
│   │   │       ├── config/      # 配置类（拦截器、MyBatis配置等）
│   │   │       ├── controller/  # 控制器层
│   │   │       ├── domain/      # 实体类
│   │   │       ├── dto/         # 数据传输对象
│   │   │       ├── mapper/      # MyBatis Mapper接口
│   │   │       └── service/     # 业务逻辑层
│   │   └── resources/
│   │       ├── application.properties  # 应用配置
│   │       └── mybatis/mapper/   # MyBatis XML映射文件
│   └── test/                    # 测试代码
├── web/EasyStaff_web/           # 前端项目
│   ├── src/
│   │   ├── api/                 # API接口定义
│   │   ├── components/          # 公共组件
│   │   ├── layouts/             # 布局组件
│   │   ├── router/              # 路由配置
│   │   ├── utils/               # 工具类
│   │   └── views/               # 页面视图
│   ├── package.json
│   └── vite.config.ts
├── easystaff.sql                # 数据库初始化脚本
├── pom.xml                      # Maven配置文件
└── README.md                    # 项目说明文档
```

## ✨ 功能特性

- ✅ **用户认证**: 用户登录、登出功能，基于 Session 的会话管理
- ✅ **员工管理**:
  - 员工信息列表查询（支持分页）
  - 条件查询（按姓名、职位等）
  - 新增员工
  - 删除员工
  - 查看员工详情
- ✅ **权限控制**: 登录拦截器保护需要认证的接口
- ✅ **响应式设计**: 前端采用 Element Plus 组件库，界面美观易用

## 🔧 环境要求

### 后端环境

- JDK 21 或更高版本
- Maven 3.6+
- MySQL 5.7+ 或 MySQL 8.0+

### 前端环境

- Node.js >= 22.14.0
- npm >= 10.9.2 或 pnpm >= 10.15.1

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone <https://github.com/genwilliam/EasyStaff.git>
cd EasyStaff
```

### 2. 数据库配置

#### 2.1 创建数据库

执行 `easystaff.sql` 脚本创建数据库和表：

```bash
mysql -u root -p < easystaff.sql
```

或者手动执行 SQL 脚本中的语句。

#### 2.2 修改数据库配置

编辑 `src/main/resources/application.properties`，修改数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/easystaff?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. 启动后端服务

```bash
# 使用 Maven Wrapper
./mvnw spring-boot:run

# 或使用已安装的 Maven
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`

### 4. 启动前端服务

```bash
cd web/EasyStaff_web

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务默认运行在 `http://localhost:5173`

### 5. 访问系统

自动打开浏览器访问 `http://localhost:5173`

**默认管理员账户**:

- 用户名: `admin`
- 密码: `123456`

## 📝 配置说明

### 后端配置

主要配置文件：`src/main/resources/application.properties`

- `spring.datasource.*`: 数据库连接配置
- `mybatis.mapper-locations`: MyBatis Mapper XML 文件位置
- `mybatis.type-aliases-package`: 实体类包路径
- `logging.level.*`: 日志级别配置

### 前端配置

主要配置文件：`web/EasyStaff_web/vite.config.ts`

- `server.proxy`: API 代理配置，将 `/api` 请求代理到后端服务
- `resolve.alias`: 路径别名配置

## 📡 API 接口文档

### 用户相关接口

#### 用户登录

- **URL**: `POST /api/login`
- **请求体**:
  ```json
  {
    "username": "admin",
    "password": "123456"
  }
  ```
- **响应**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员"
    }
  }
  ```

#### 用户登出

- **URL**: `POST /api/logout`
- **响应**:
  ```json
  {
    "code": 200,
    "message": "已退出登录"
  }
  ```

### 员工相关接口

#### 分页查询员工列表

- **URL**: `GET /api/employees`
- **查询参数**:
  - `page`: 页码（默认 1）
  - `pageSize`: 每页数量（默认 10）
  - `name`: 员工姓名（可选，模糊查询）
  - `position`: 职位（可选，模糊查询）
- **响应**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "list": [...],
      "total": 100,
      "page": 1,
      "pageSize": 10
    }
  }
  ```

#### 新增员工

- **URL**: `POST /api/employees`
- **请求体**:
  ```json
  {
    "name": "张三",
    "age": 28,
    "position": "开发工程师",
    "entryDate": "2025-01-05"
  }
  ```

#### 删除员工

- **URL**: `DELETE /api/employees/{id}`

#### 查看员工详情

- **URL**: `GET /api/employees/{id}`

## 🛡️ 权限控制

系统使用 `LoginInterceptor` 拦截器对需要认证的接口进行保护。未登录用户访问受保护的接口将被重定向到登录页面。

受保护的接口路径配置在 `WebMvcConfig` 中，默认保护 `/api/employees/**` 路径。

## 🧪 开发说明

### 后端开发

- 使用 MyBatis 进行数据库操作
- 使用 Lombok 简化实体类代码
- 使用 Spring Validation 进行参数校验
- 统一使用 `ApiResponse` 封装 API 响应

### 前端开发

- 使用 TypeScript 进行类型检查
- 使用 Element Plus 组件库构建 UI
- 使用 Axios 进行 HTTP 请求
- 使用 Vue Router 进行路由管理

### 代码规范

前端项目配置了 ESLint 和 Prettier，运行以下命令进行代码检查和格式化：

```bash
# 代码检查
npm run lint:eslint

# 代码格式化
npm run lint:prettier
```

## 📦 构建部署

### 后端构建

```bash
mvn clean package
```

构建产物位于 `target/EasyStaff-0.0.1-SNAPSHOT.jar`

### 前端构建

```bash
cd web/EasyStaff_web
npm run build
```

构建产物位于 `web/EasyStaff_web/dist`

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 [MIT](LICENSE) 许可证。

## 👤 作者

William

## 🙏 致谢

感谢以下开源项目：

- Spring Boot
- Vue.js
- Element Plus
- MyBatis
- 以及其他所有依赖的开源项目
