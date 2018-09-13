# spring-boot-admin-in-docker
在docker中部署一个由spring boot admin管理的服务集群 

Deploying a service cluster managed by spring boot admin in docker

[Spring Boot admin document](http://codecentric.github.io/spring-boot-admin/2.0.2/#getting-started)

[使用文档](https://github.com/liumapp/spring-boot-admin-in-docker/wiki)

## 指南

### 1.1 最简化环境启动

详细操作请点击[这里](https://github.com/liumapp/spring-boot-admin-in-docker/wiki/1.1-%E6%9C%80%E7%AE%80%E5%8C%96%E7%8E%AF%E5%A2%83%E5%90%AF%E5%8A%A8)

### 1.2 添加服务日志

详细操作请点击[这里](https://github.com/liumapp/spring-boot-admin-in-docker/wiki/1.2-%E6%B7%BB%E5%8A%A0%E6%9C%8D%E5%8A%A1%E6%97%A5%E5%BF%97)

### 1.3 配置Spring Security

详细操作请点击[这里](https://github.com/liumapp/spring-boot-admin-in-docker/wiki/1.3-%E9%85%8D%E7%BD%AESpring-Security)

首先请将项目切换到v1.3.0版本

    git checkout v1.3.0
    
在之前的版本中，我们并没有引入spring security

这意味着admin-server管理控制台随便是谁都可以登录，这在本地开放环境下是没有什么影响的

但是如果发布到线上呢？

所以接下来要实现的功能，就是给admin-server添加一个登录登出的界面跟按钮

相关效果如下图

         