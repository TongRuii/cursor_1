# 企业风控检查服务

本项目实现了一个灵活可配置的企业风控检查服务，可以根据提供的企业名称或统一社会信用代码，调用第三方服务（如企查查、天眼查等）查询企业的工商信息、失信记录、被执行人记录、司法风险等数据。

## 功能特点

- **灵活配置**: 支持通过配置选择使用的第三方服务，开启或关闭服务
- **配置热更新**: 配置通过Nacos实时更新，无需重启服务
- **多服务支持**: 当前支持企查查、天眼查等多种第三方数据服务
- **智能路由**: 根据配置权重自动选择可用的第三方服务
- **失败重试**: 支持请求失败后的自动重试机制
- **数据整合**: 提供统一的数据模型，整合不同来源的数据

## 技术架构

- **Spring Boot**: 基础框架
- **Spring Cloud**: 服务治理
- **Nacos**: 配置中心
- **OpenFeign**: 调用第三方服务
- **RestTemplate**: HTTP请求客户端

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── riskcontrol/
│   │           ├── client/             # 第三方服务客户端
│   │           │   └── impl/           # 具体实现
│   │           ├── config/             # 配置类
│   │           ├── controller/         # 控制器
│   │           ├── model/              # 数据模型
│   │           ├── service/            # 服务接口
│   │           │   └── impl/           # 服务实现
│   │           └── util/               # 工具类
│   └── resources/
│       ├── application.yml             # 应用配置
│       ├── bootstrap.yml               # 启动配置
│       └── nacos-config-example.yaml   # Nacos配置示例
```

## 快速开始

### 前置条件

- JDK 11+
- Maven 3.6+
- Nacos 服务

### 配置说明

在 Nacos 中配置以下内容（参考 `nacos-config-example.yaml`）:

```yaml
# 风控服务配置
risk-control:
  # 是否启用风控服务
  enabled: true
  # 启用的第三方服务列表（按顺序）
  enabled-services:
    - qichacha
    - tianyan
  # 第三方服务配置
  third-party:
    # 企查查配置
    qichacha:
      name: 企查查
      enabled: true
      url: https://api.qichacha.com
      auth:
        type: apiKey
        api-key: your-api-key
        api-secret: your-api-secret
      timeout: 5000
      retry-times: 3
      weight: 2
    # 天眼查配置
    tianyan:
      name: 天眼查
      enabled: true
      url: https://api.tianyancha.com
      auth:
        type: token
        token: your-token
      timeout: 5000
      retry-times: 3
      weight: 1
```

### API接口

#### 查询企业风控信息

```
POST /api/enterprise/risk/query
```

请求体:

```json
{
  "enterpriseName": "公司名称",
  "creditCode": "统一社会信用代码"
}
```

> 注：企业名称和统一社会信用代码至少提供一个

#### 使用指定服务查询企业风控信息

```
POST /api/enterprise/risk/query/{serviceName}
```

参数:
- `serviceName`: 服务名称，如 `qichacha`、`tianyan`

请求体同上。

## 二次开发

### 添加新的第三方服务

1. 实现 `EnterpriseRiskClient` 接口
2. 在 Nacos 配置中添加新服务的配置
3. 在服务列表中启用新服务

### 扩展数据模型

可以在 `model` 包下扩展更多的数据模型，以支持更丰富的业务场景。

## 生产环境部署

1. 修改 `application.yml` 和 `bootstrap.yml` 中的相关配置
2. 打包: `mvn clean package`
3. 运行: `java -jar risk-control-service.jar`

## 注意事项

- 请确保第三方服务的API密钥正确且有效
- 生产环境请使用HTTPS协议保证数据传输安全
- 建议对敏感配置进行加密处理 