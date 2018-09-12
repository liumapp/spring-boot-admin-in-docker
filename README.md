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

首先请clone项目到本地

随后使用git命令切换到v1.0.1的tag

    git checkout v1.0.1
    
在这个版本下，我们基于v1.0.0的版本，对admin-client进行了一些配置上的改动：
    
    logging:
      file: /usr/local/tomcat/project/spring-boot-admin-in-docker/log/admin-client.log
      pattern:
        file: "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID}){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx"
    management:
      endpoints:
        web:
          exposure:
            include: "*"    
            
上面的改动中，我们通过management开放了所有的actuator节点信息，因为在springboot2.x系列下，默认只开放了三个，如果您希望在admin-server中查询到详细信息，您需要全部开启他们

然后logging.file指向日志文件的存放地址，请确保该目录具有可写权限

之后再依次运行admin-eureka、admin-client跟admin-server

访问浏览器的 http://localhost:8766/ ，在点击admin-client进入详情页，我们可以发现所有的配置信息包括日志信息将会罗列出来





           