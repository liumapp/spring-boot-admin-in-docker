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

### 1.4 给Eureka也上一把锁

1.3节中，我们利用spring security给admin-server加了一把"锁"，但是却忽视了作为服务注册中心的Eureka，这在生产环境中是万万不可的

请将项目代码切换到v1.4.0版本

    git checkout v1.4.0
    
    





