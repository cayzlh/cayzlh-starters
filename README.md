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

**[GITHUB](https://github.com/cayzlh/cayzlh-starters)**

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

### base-spring-boot-starter

### jwt-spring-boot-starter

### redis-distributedlock-spring-boot-starter

### redisson-distributedlock-spring-boot-starter

### zk-distributedlock-spring-boot-starter

