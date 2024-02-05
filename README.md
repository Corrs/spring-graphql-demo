# 概述
> 基于GraphQL的开发实践

## 选型
- java:17
- spring-boot:2.7.6
- spring-security
- spring-graphql
- redis
- mysql
- mybatis-plus
- java-jwt

## 自定义scalar
- Upload，用于支持文件上传(**后端必须使用集合接收文件数据**)
- LocalDateTime，用于支持日期类型，接收和返回**yyyy-MM-dd HH:mm:ss格式的字符串**

## redis cache增强
>  支持 # 号分隔 cachename 和 超时，支持 ms（毫秒），s（秒默认），m（分），h（小时），d（天）等单位。
```java
@Cacheable(cacheNames = "helloLocalDateTime#1m")
public LocalDateTime helloLocalDateTime() {
    return LocalDateTime.now();
}
```

## docker快速启动
cache目录存放redis的配置文件
db目录存放mysql配置文件、镜像构建文件和初始化脚本
```shell
docker-compose up -d
```
