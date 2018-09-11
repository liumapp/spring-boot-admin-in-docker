# spring-boot-admin-in-docker
在docker中部署一个由spring boot admin管理的服务集群 Deploying a service cluster managed by spring boot admin in docker

under coding , plz do not use right now . 

## how to use

for more detail , plz read [document](http://codecentric.github.io/spring-boot-admin/2.0.2/#getting-started)

### 搭建

按照以下配置，您可以在五分钟之内完成环境的搭建，运行结果如下图所示



#### Eureka

首先要把Eureka跑起来，然后让其他的服务：client和server注册到Eureka上

eureka添加以下依赖：

    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    
配置文件内容：

    server:
      port: 1234
    
    spring:
      cloud:
        client:
          ipAddress: localhost
    eureka:
      instance:
        hostname: localhost
        prefer-ip-address: false
      client:
        register-with-eureka: false
        fetch-registry: false
        service-url:
          defaultZone: http://localhost:1234/eureka/
          
启动类代码：
 
    @SpringBootApplication
    @EnableEurekaServer
    public class AdminEurekaMain {
    
         public static void main (String[] args) {
             SpringApplication.run(AdminEurekaMain.class, args);
         }
    
    }   
    
#### client

接下来我们跑一个client应用

maven依赖

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.jolokia</groupId>
      <artifactId>jolokia-core</artifactId>
    </dependency>
    
请注意，如果我们不添加jolokia-core的话，client的状态对于server而言将不会实时更新

application.yml配置
    
    server:
      port: 7373
    
    spring:
      application:
        name: admin-client
      cloud:
        client:
          ipAddress: localhost
    
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:1234/eureka/
    
启动类代码

    @SpringBootApplication
    @EnableDiscoveryClient
    public class AdminClientMain {
    
        public static void main (String[] args) {
            SpringApplication.run(AdminClientMain.class, args);
        }
    
    }
    
### server

server端是用于监测所有服务的运行状态

Maven依赖

    <dependency>
      <groupId>de.codecentric</groupId>
      <artifactId>spring-boot-admin-starter-server</artifactId>
      <version>2.0.2</version>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
application.yml配置

    server:
      port: 8766
    spring:
      application:
        name: admin-server
      cloud:
        client:
          ipAddress: localhost
    
    eureka:
      client:
        registryFetchIntervalSeconds: 5
        service-url:
          defaultZone: http://localhost:1234/eureka/
      instance:
        leaseRenewalIntervalInSeconds: 10
        health-check-url-path: /actuator/health
    
    management:
      endpoints:
        web:
          exposure:
            include: "*"
      endpoint:
        health:
          show-details: ALWAYS    

启动类代码

    @Configuration
    @EnableAutoConfiguration
    @EnableDiscoveryClient
    @EnableAdminServer
    public class AdminServerMain {
    
        public static void main(String[] args) {
            SpringApplication.run(AdminServerMain.class, args);
        }
    
        @Configuration
        public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().anyRequest().permitAll()
                        .and().csrf().disable();
            }
        }
    
    }                    