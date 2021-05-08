<p align="center">
  <a href="https://github.com/cayzlh/cayzlh-starters">
   <img alt="spring-boot-plus logo" style="border-radius: 50%;" src="https://cdn.jsdelivr.net/gh/cayzlh/git-img-repository@master/2020/04/26/VWLI6t.png">
  </a>
</p>

<p align="center">  
  <a href="https://github.com/cayzlh/cayzlh-starters/">
    <img alt="cayzlh-starters version" src="https://img.shields.io/badge/cayzlh--starters-1.0.x-blue">
  </a>
  <a href="https://github.com/spring-projects/spring-boot">
    <img alt="spring boot version" src="https://img.shields.io/badge/spring%20boot-2.2.1.RELEASE-brightgreen">
  </a>
  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>

## cayzlh-starters是一套根据自己的兴趣开发的starter集合

> 整理开发过程中觉得好用的插件或功能，制作成`starter`以方便以后开发`spring-boot`项目

## 简介

> 根据自己的开发习惯，将工作开发中常用的功能开发成若干`starter`，减少新项目的开发量，`spring-boot`项目引入之后只需要做少量的配置即可使用。

**[GITHUB](https://github.com/cayzlh/cayzlh-starters)** ｜ **[MAVEN仓库](https://github.com/cayzlh/maven-repo)**

### 主要模块

| 模块                                         | 描述                    |
| -------------------------------------------- | ----------------------- |
| commons                                      | 通用功能模块            |
| base-spring-boot-starter                     | 基础框架能力starter     |
| jwt-spring-boot-starter                      | JWT-Shiro模块           |
| redis-distributedlock-spring-boot-starter    | 基于redis的分布式锁     |
| redisson-distributedlock-spring-boot-starter | 基于redisson的分布式锁  |
| zk-distributedlock-spring-boot-starter       | 基于zookeeper的分布式锁 |

### 项目结构

```
cayzlh-starters
├── commons
├── base-spring-boot-starter-parent
│   ├── base-spring-boot-autoconfig
│   ├── base-spring-boot-sample
│   ├── base-spring-boot-starter
│   └── pom.xml
├── jwt-spring-boot-starter-parent
│   ├── jwt-spring-boot-autoconfig
│   ├── jwt-spring-boot-sample
│   ├── jwt-spring-boot-starter
│   └── pom.xml
├── redis-distributedlock-spring-boot-starter-parent
│   ├── pom.xml
│   ├── redis-distributedlock-spring-boot-autoconfig
│   ├── redis-distributedlock-spring-boot-sample
│   └── redis-distributedlock-spring-boot-starter
├── redisson-distributedlock-spring-boot-starter-parent
│   ├── pom.xml
│   ├── redisson-distributedlock-spring-boot-autoconfig
│   ├── redisson-distributedlock-spring-boot-sample
│   └── redisson-distributedlock-spring-boot-starter
└── zk-distributedlock-spring-boot-starter-parent
    ├── pom.xml
    ├── zk-distributedlock-spring-boot-autoconfig
    ├── zk-distributedlock-spring-boot-sample
    └── zk-distributedlock-spring-boot-starter
```

### 项目构建环境

| 中间件 | 版本  | 备注         |
| ------ | ----- | ------------ |
| JDK    | 1.8+  | JDK1.8及以上 |
| Redis  | 5.0.6 |              |

## 分模块介绍

### commons

> 通用模块

定义一些公共接口、常量类、工具类等

### base-spring-boot-starter

> 提供基础功能的starter

#### **自动封装返回结果**

通过`ResponseBodyAdvice`实现返回结果的自动包装。

*是否将转换成统一的返回格式 。*

*如：{"msg":"completed success.","code":0,"data":"test1","requestId":"6892435279"}*

可通以下配置开启：

```yaml
cayzlh:
  framework:
    base:
      convertable: false # 默认为true
```

*举个栗子：*

VO：

```java
@Data
@Builder
public class TestVo {

    private Integer id;

    private String name;

}
```

controller：

```java
@GetMapping("/test3")
public TestVo test3() {
  return TestVo.builder().id(1).name("test3").build();
}
```

请求结果：

```java
{
  "requestId": "8275300804",
  "code": 0,
  "msg": "completed success.",
  "data": {
    "test": "test1"
  }
}
```

#### requestId

为每个请求添加**requestId**，方便日后日志查找：

```bash
cat **.log | grep yourrequestId
```

当然，在请求结果里面也会包装以恶搞requestId字段，编码过程中也可以通过`BaseContextHolder`来获得：

```java
BaseContextHolder.getRequestId();
```

#### 日志

- 除了上述的`requestId`之外，默认还打印所有请求的请求参数与返回结果。
- 可通过配置文件配置日志输出级别
- 可通过配置文件指定日志文件的保存位置
- 打印每次请求的日志（请求参数、返回结果）

**展示效果：**

![image-20200426215822991](https://cdn.jsdelivr.net/gh/cayzlh/git-img-repository@master/2020/04/26/Jt6ACI.png)

**部分配置：**

```yaml
cayzlh:
  framework:
    base:
      log:
        level: INFO	# 输出日志级别 默认为INFO
        path: logs	# 日志输出路径 默认为logs
        enable: true # 是否启用请求日志记录，默认开启
        exclude-paths: # 排除不打印请求日志的请求路径
         - /xxx
         - /yyy
        request-log-format: true	#是否格式化输出请求日志
        response-log-format: true	# 是否格式话请求响应日志 
```

**由于日志记录是使用`Aop`实现的，使用的时候需要自己实现一个继承`BaseLogAop`类的`Aop`类，并开启项目的`Aop`功能**

- 首先需要开启`AspectJ`能力，修改启动类，添加`@EnableAspectJAutoProxy`注解

  ```java
  @SpringBootApplication
  @EnableAspectJAutoProxy
  public class BaseSampleApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(BaseSampleApplication.class, args);
      }
  
  }
  ```

- 实现一个`LogAop`类，并交给`Spring`管理，可参考：[LogAop](https://github.com/cayzlh/cayzlh-starters/blob/dev/base-spring-boot-starter-parent/base-spring-boot-sample/src/main/java/com/cayzlh/framewrok/base/sample/aop/LogAop.java)

> 这一步的主要目的是需要使用方定义符合自己实际应用的切面，剩下的就交给`BaseLogAop`去完成。

```java
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(value = 
                       {"cayzlh.framework.base.log.enable"},
                       matchIfMissing = true)
public class LogAop extends BaseLogAop {

    private static final String POINTCUT =
            "execution(public * com.cayzlh.jwt.controller.*.*(..))";

    @Pointcut(POINTCUT)
    private void log() {

    }

    @Around("log()")
    @Override
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.handle(joinPoint);
    }

    @AfterThrowing(pointcut = "log()", throwing = "exception")
    @Override
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        super.handleAfterThrowing(exception);
    }

    @Override
    protected void setRequestId(RequestInfo requestInfo) {
        super.handleRequestId(requestInfo);
    }

    @Override
    protected void getRequestInfo(RequestInfo requestInfo) {
        super.handleRequestInfo(requestInfo);
    }

    @Override
    protected void getResponseResult(Object result) {
        super.handleResponseResult(result);
    }

    @Override
    protected void finish(RequestInfo requestInfo, 
                          OperationInfo operationInfo, Object result,
            Exception exception) {
        log.debug("do log aop finish..");
        // 如果需要将请求信息保存到数据库之类的，就在这个地方写
    }
```

### jwt-spring-boot-starter

> 提供`jwt`能力，`jwt`与`shiro`整合

引入这个`starter`即可使用默认的`jwt`与`shiro`能力。

部分配置如下：

```yaml
cayzlh:
  framework:
    jwt:
      secret: 123456789
      issuer: cayzlh
      subject: token
      audience: users
      redis-check: true
      single-login: true
      salt-check: true
    shiro:
      enable: true
      anon:
        - /app/login, /app/logout
```

部分API参考如下：

```java
JwtToken jwtToken = JwtToken.build(token,username,newSalt);

ShiroUtil.login(jwtToken);

loginRedisService.cacheLoginInfo(jwtToken, loginUser);
log.debug("login success: {}", username);
```

**example:**[LoginServiceImpl.java](https://github.com/cayzlh/cayzlh-starters/blob/dev/jwt-spring-boot-starter-parent/jwt-spring-boot-sample/src/main/java/com/cayzlh/jwt/service/impl/LoginServiceImpl.java)

### redis-distributedlock-spring-boot-starter

> 用基于`redis`的分布式锁

#### 必要的配置

```yaml
spring:
  redis:
    host: localhost
    database: 2
    port: 6379
    lettuce:
      pool:
        max-active: 8
        min-idle: 0
        max-idle: 8
        max-wait: 10000ms
      shutdown-timeout: 100ms
```

#### 在代码中使用

使用默认的`key`：

```java
@RedisLock
public String test1() throws InterruptedException {
  Thread.sleep(10000);
  return "test1";
}
```

自定义`key`，支持`EL`表达式：

```java
@RedisLock(key = "'test121312312312'.concat(#num)")
public String test2(@RequestParam Integer num) throws InterruptedException {
  Thread.sleep(10000);
  return "test2:"+num;
}
```

### redisson-distributedlock-spring-boot-starter

> 基于`redisson`的分布式锁

#### 必要的配置

```yaml
spring:
  redis:
    database: 2
    host: localhost
    password:
    port: 6379
    timeout: 180000
    redisson:
      config: classpath:redisson.json
```

#### 在代码中使用

默认`key`：

```java
@RedissonLock
public String test1() throws InterruptedException {
  Thread.sleep(10000);
  return "test1";
}
```

自定义`key`：

```java
@RedissonLock(key = "'test121312312312'.concat(#num)")
public String test2(@RequestParam Integer num) throws InterruptedException {
  Thread.sleep(10000);
  return "test2:"+num;
}
```

### zk-distributedlock-spring-boot-starter

> 基于`zookeeper`的分布式锁

#### 必要的配置

```yaml
cayzlh:
  framework:
    zk:
      connect-string: 127.0.0.1:2181
      namespace: distribute_lock
```

#### 在代码中使用

```java
@ZkLock
public String test1() throws InterruptedException {
  Thread.sleep(10000);
  return "test1";
}
```

## License

[MIT License](https://github.com/cayzlh/cayzlh.github.io/blob/master/LICENSE)

Copyright (c) 2020 cayzlh
