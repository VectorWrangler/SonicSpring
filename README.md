# Sonic Music Platform

Sonic Music Platform 是一个基于Spring Boot开发的音乐平台后端系统，提供了音乐搜索、播放、歌单管理等功能。

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **编程语言**: Java 17
- **数据库**: MySQL
- **ORM框架**: MyBatis Plus
- **API文档**: Swagger/OpenAPI
- **构建工具**: Maven

## 功能特性

- 🎵 音乐搜索（支持单曲、专辑、歌手、歌单、MV搜索）
- 🎧 音乐播放（支持歌曲和MV播放）
- 📋 歌单管理
- 🔍 分页查询支持
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
├── mapper/          # MyBatis Mapper接口
├── service/         # 服务层接口
└── service/impl/    # 服务层实现
```

## API接口

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