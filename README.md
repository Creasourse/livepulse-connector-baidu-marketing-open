# Baidu Marketing Connector

百度营销平台连接器，用于推送人群包数据到百度营销平台。

## 版本信息

- **版本**: v1.0
- **描述**: 推送人群包到百度营销平台，支持MD5加密手机号、IMEI、IDFA、OAID等多种ID类型及格式验证
- **分类**: Marketing
- **端口**: 23015

## 功能特性

- 多账号管理
- 人群包管理
- 用户数据管理（支持多种ID类型）
- 自动格式验证和MD5加密
- 批量数据推送
- 定时同步任务
- Webhook 事件订阅
- RESTful API 接口
- Swagger 文档支持

## 数据模型

### 核心表

- `baidu_marketing_account` - 账户配置表
- `baidu_marketing_audience` - 人群包表
- `baidu_marketing_user_data` - 用户数据表
- `baidu_marketing_webhook_log` - Webhook 日志表
- `baidu_marketing_sync_log` - 同步日志表

## 支持的ID类型

- **PHONE** - 手机号明文（11位数字，自动转换为MD5_PHONE）
- **MD5_PHONE** - MD5加密后的手机号（32位小写十六进制）
- **IMEI** - IMEI明文（15位数字，自动转换为MD5_IMEI）
- **MD5_IMEI** - MD5加密后的IMEI（32位小写十六进制）
- **IDFA** - IDFA明文（8-4-4-4-12格式，自动转换为MD5_IDFA）
- **MD5_IDFA** - MD5加密后的IDFA（32位小写十六进制）
- **OAID** - OAID明文（16位十六进制，自动转换为MD5_OAID）
- **MD5_OAID** - MD5加密后的OAID（32位小写十六进制）

## 快速开始

### 1. 数据库初始化

```bash
psql -U baidu_user -d livepulse_baidu_marketing -f docs/sql/baidu_marketing_schema.sql
```

### 2. 配置 Nacos

在 Nacos 中创建以下配置：

- `postgresql-config.yml` - 数据库配置
- `kafka-config.yml` - Kafka 配置

### 3. 启动应用

```bash
java -jar livepulse-connector-baidu-marketing-open.jar \
  --spring.profiles.active=prod \
  --NACOS_SERVER_ADDR=nacos-host:8848
```

### 4. 访问 API 文档

```
http://localhost:23015/swagger-ui.html
```

## API 端点

### 账户管理

- `POST /baidu-marketing/account` - 添加账户配置
- `PUT /baidu-marketing/account/{id}` - 更新账户配置
- `DELETE /baidu-marketing/account/{id}` - 删除账户配置
- `GET /baidu-marketing/account/{id}` - 获取账户配置
- `GET /baidu-marketing/account` - 分页查询账户列表
- `POST /baidu-marketing/account/{id}/enable` - 启用账户
- `POST /baidu-marketing/account/{id}/disable` - 禁用账户
- `POST /baidu-marketing/account/{id}/enable-webhook` - 启用 Webhook
- `POST /baidu-marketing/account/{id}/disable-webhook` - 禁用 Webhook
- `GET /baidu-marketing/account/list-all` - 获取所有启用的账户

### 人群包管理

- `POST /baidu-marketing/audience` - 添加人群包
- `PUT /baidu-marketing/audience/{id}` - 更新人群包
- `DELETE /baidu-marketing/audience/{id}` - 删除人群包
- `GET /baidu-marketing/audience/{id}` - 获取人群包详情
- `GET /baidu-marketing/audience` - 分页查询人群包列表
- `POST /baidu-marketing/audience/{id}/process` - 标记为已处理
- `POST /baidu-marketing/audience/{id}/sync` - 同步人群包到百度
- `GET /baidu-marketing/audience/account/{accountId}` - 获取账户的所有人群包

### 用户数据管理

- `POST /baidu-marketing/user-data` - 添加用户数据（自动验证并加密）
- `POST /baidu-marketing/user-data/batch` - 批量添加用户数据
- `GET /baidu-marketing/user-data/{id}` - 获取用户数据详情
- `GET /baidu-marketing/user-data` - 分页查询用户数据
- `POST /baidu-marketing/user-data/{id}/process` - 标记为已处理
- `GET /baidu-marketing/user-data/audience/{audienceId}/valid-count` - 获取有效用户数量
- `GET /baidu-marketing/user-data/audience/{audienceId}/unprocessed-count` - 获取未处理用户数量
- `POST /baidu-marketing/user-data/audience/{audienceId}/mark-all-processed` - 标记所有未处理数据为已处理

### 日志查询

- `GET /baidu-marketing/webhook-log/{id}` - 获取 Webhook 日志详情
- `GET /baidu-marketing/webhook-log` - 分页查询 Webhook 日志列表
- `POST /baidu-marketing/webhook-log/{id}/mark-processed` - 标记为已处理
- `POST /baidu-marketing/webhook-log/{id}/mark-failed` - 标记为失败
- `POST /baidu-marketing/webhook-log/{id}/retry` - 重试处理
- `GET /baidu-marketing/webhook-log/account/{accountId}` - 获取账户的所有 Webhook 日志
- `GET /baidu-marketing/webhook-log/recent` - 获取最近的 Webhook 日志

- `GET /baidu-marketing/sync-log/{id}` - 获取同步日志详情
- `GET /baidu-marketing/sync-log` - 分页查询同步日志列表
- `GET /baidu-marketing/sync-log/account/{accountId}` - 获取账户的同步日志
- `GET /baidu-marketing/sync-log/audience/{audienceId}` - 获取人群包的同步日志
- `GET /baidu-marketing/sync-log/recent` - 获取最近的同步日志
- `GET /baidu-marketing/sync-log/running` - 获取正在运行的同步任务

## 数据格式示例

### 添加账户配置

```json
POST /baidu-marketing/account
{
  "accountId": "baidu_marketing_123456789",
  "accountName": "测试百度营销账户",
  "apiKey": "your-api-key-here",
  "accessToken": "your-access-token-here",
  "accountType": "SEARCH",
  "industryType": "TECHNOLOGY",
  "companyName": "测试公司"
}
```

### 添加人群包

```json
POST /baidu-marketing/audience
{
  "accountId": 1,
  "audienceId": "audience_001",
  "audienceName": "测试人群包",
  "audienceType": "CUSTOM",
  "audienceDescription": "这是一个测试人群包",
  "idType": "MD5_PHONE",
  "encryptionType": "MD5"
}
```

### 添加用户数据（自动加密）

```json
POST /baidu-marketing/user-data
{
  "audienceId": 1,
  "idType": "PHONE",
  "idValue": "13800138000",
  "encryptionType": "PLAIN"
}
```

系统会自动将手机号转换为 MD5 加密格式：
- idType 自动变为 `MD5_PHONE`
- idValue 自动变为 MD5 加密后的32位小写十六进制字符串

### 批量添加用户数据

```json
POST /baidu-marketing/user-data/batch
[
  {
    "audienceId": 1,
    "idType": "PHONE",
    "idValue": "13800138001"
  },
  {
    "audienceId": 1,
    "idType": "MD5_IMEI",
    "idValue": "868123456789012"
  },
  {
    "audienceId": 1,
    "idType": "IDFA",
    "idValue": "2F0C5F92-7E3A-4B2D-9A1C-7E8F9B2C3D4E"
  }
]
```

## Docker 部署

```bash
# 构建镜像
docker build -t livepulse-connector-baidu-marketing:1.0 .

# 启动容器
docker-compose up -d

# 查看日志
docker-compose logs -f
```

## 配置说明

### bootstrap.yml

```yaml
server:
  port: 23015

spring:
  application:
    name: baidu-marketing-open-connector-server

baidu:
  marketing:
    api:
      version: 1.0
      timeout: 30
      base-url: https://api.baidu.com
    sync:
      batch-size: 100
      max-retries: 3
    encryption:
      default-type: MD5
      auto-encrypt: true
      validate-format: true
```

## 环境要求

- Java 17
- Maven 3.8+
- PostgreSQL 15+
- Nacos 2.5.1+

## 文档

详细部署文档请参考：[部署指南.md](部署指南.md)

## 许可证

Copyright © 2025 Livepulse
