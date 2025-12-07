# Sonic Music Platform

Sonic Music Platform 是一个基于Spring Boot开发的音乐平台后端系统，提供了音乐搜索、播放、歌单管理等功能。

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **编程语言**: Java 17
- **数据库**: MySQL
- **ORM框架**: MyBatis Plus
- **安全框架**: Spring Security + JWT
- **日志框架**: Logback
- **API文档**: Swagger/OpenAPI
- **构建工具**: Maven

## 功能特性

- 🎵 音乐搜索（支持单曲、专辑、歌手、歌单、MV搜索）
- 🎧 音乐播放（支持歌曲和MV播放）
- 📋 歌单管理
- 🔍 分页查询支持
- 🔐 用户认证与授权（JWT）
- 👨‍💼 后台管理功能
- 📊 日志记录功能
- 🖼️ 头像上传功能
- 📱 RESTful API设计
- 📘 自动API文档生成

## 项目结构

```
src/main/java/cn/edu/hbnu/sonic/
├── common/          # 通用工具类和返回结果封装
├── config/          # 配置类（Swagger、CORS、MyBatis Plus等）
├── controller/      # 控制器层
├── dto/             # 数据传输对象
├── entity/          # 实体类
├── filter/          # 过滤器
├── mapper/          # MyBatis Mapper接口
├── service/         # 服务层接口
├── service/impl/    # 服务层实现
├── service/admin/   # 管理员服务
└── util/           # 工具类
```

## API接口

### 认证接口
- `POST /auth/register` - 用户注册（用户名和密码必填，其他字段可选，支持头像上传）
- `POST /auth/login` - 用户登录

### 搜索接口
- `GET /search` - 音乐内容搜索
  - 参数:
    - `keywords`: 搜索关键词（必填）
    - `limit`: 返回数量，默认30
    - `offset`: 偏移量，默认0
    - `type`: 搜索类型（1:单曲, 10:专辑, 100:歌手, 1000:歌单, 1002:MV）

### 歌曲接口
- `GET /song/allSongs` - 获取所有歌曲
- `GET /song/url` - 根据ID获取歌曲详情

### MV接口
- `GET /mv/all` - 获取所有MV
- `GET /mv/url` - 根据ID获取MV详情

### 歌单接口
- `GET /playlist` - 获取所有歌单
- `GET /playlist/{id}` - 根据ID获取歌单详情
- `GET /playlist/track/all` - 获取歌单中的歌曲

### 管理员接口
- `GET /admin/users` - 获取所有用户
- `GET /admin/users/{id}` - 根据ID获取用户详情
- `DELETE /admin/users/{id}` - 删除用户
- `PUT /admin/users/{id}/role` - 更新用户角色
- `GET /admin/songs` - 获取所有歌曲
- `POST /admin/songs` - 添加歌曲
- `PUT /admin/songs/{id}` - 更新歌曲
- `DELETE /admin/songs/{id}` - 删除歌曲
- `GET /admin/artists` - 获取所有艺术家
- `POST /admin/artists` - 添加艺术家
- `PUT /admin/artists/{id}` - 更新艺术家
- `DELETE /admin/artists/{id}` - 删除艺术家
- `GET /admin/albums` - 获取所有专辑
- `POST /admin/albums` - 添加专辑
- `PUT /admin/albums/{id}` - 更新专辑
- `DELETE /admin/albums/{id}` - 删除专辑
- `GET /admin/mvs` - 获取所有MV
- `POST /admin/mvs` - 添加MV
- `PUT /admin/mvs/{id}` - 更新MV
- `DELETE /admin/mvs/{id}` - 删除MV
- `GET /admin/playlists` - 获取所有歌单
- `DELETE /admin/playlists/{id}` - 删除歌单

## 安全配置

项目使用Spring Security和JWT进行安全控制：

- 公开接口（无需认证）：
  - `/search/**` - 搜索接口
  - `/song/url` - 歌曲详情接口
  - `/song/allSongs` - 获取所有歌曲接口
  - `/mv/url` - MV详情接口
  - `/swagger-ui/**` - Swagger UI
  - `/v3/api-docs/**` - API文档
  - `/auth/**` - 认证接口

- 受保护接口（需要认证）：
  - 除上述公开接口外的所有接口都需要JWT Token认证

- 管理员接口（需要ADMIN角色）：
  - `/admin/**` - 所有管理员接口

## 文件上传配置

项目支持文件上传功能，默认配置如下：

- 上传文件存储位置：媒体服务器
- 头像文件存储路径：`/user/avatar/`
- 数据库存储：相对路径（相对于媒体服务器）
- 支持的文件类型：图片文件（jpg, png, gif等）
- 文件命名：使用UUID生成唯一文件名

## 日志配置

项目使用Logback作为日志框架，默认配置如下：

- 日志文件存储位置：`./logs/`
- 日志级别：
  - 控制台输出：INFO及以上级别
  - 文件输出：DEBUG及以上级别
- 日志文件按天滚动，最大保留15天
- 不同级别的日志分别存储在不同的文件中：
  - debug.log：DEBUG级别日志
  - info.log：INFO级别日志
  - warn.log：WARN级别日志
  - error.log：ERROR级别日志

## 运行环境

- Java 17+
- MySQL 8.0+
- Maven 3.6+

## 配置说明

### 数据库配置
在 `src/main/resources/application.yml` 中配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/music_website?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: 123456
```

### 媒体服务器配置
```yaml
media:
  api: http://127.0.0.1:13666/
```

### 文件上传配置
```yaml
upload:
  path: ./uploads
```

## 启动项目

1. 克隆项目到本地
2. 创建MySQL数据库 `music_website`
3. 执行 `init.sql` 初始化数据库表结构
4. 修改 `application.yml` 中的数据库配置
5. 使用Maven构建项目：
   ```bash
   mvn clean install
   ```
6. 启动项目：
   ```bash
   mvn spring-boot:run
   ```

## API文档

项目集成了Swagger UI，启动后可通过以下地址访问API文档：
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## 许可证

本项目采用MIT许可证，详情请见[LICENSE](LICENSE)文件。