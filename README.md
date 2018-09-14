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

### 2.1 部署到Docker容器中运行

详细操作请点击[这里](https://github.com/liumapp/spring-boot-admin-in-docker/wiki/2.1-%E9%83%A8%E7%BD%B2%E5%88%B0Docker%E5%AE%B9%E5%99%A8%E4%B8%AD%E8%BF%90%E8%A1%8C)

接下来我们利用docker-compose将admin-client、admin-server以及admin-eureka部署到docker环境下面运行

首先请各位利用git命令，将项目代码切换到v4.1.0版本

    git checkout v4.1.0
    
版本切换后，项目目录下面会多出三个文件：build-image.sh、rm-image.sh和docker-compose.yml

* build-image.sh

    脚本文件，用于安装三个微服务(admin-client、admin-server和admin-eureka)的docker镜像
    
* rm-image.sh    

    脚本文件，用于删除三个微服务的docker镜像
    
    ps：要删除镜像，必须在镜像生成的容器处于stop状态下才可以执行
    
* docker-compose.yml

    在执行完build-image.sh之后，通过docker-compose编排工具，启动容器的配置文件
    
    具体启动命令为：
    
        docker-compose up -d
        
    停止命令为：
    
        docker-compose down
        
利用docker-compose up -d命令启动成功后，我们可以访问浏览器的admin-server界面，相关截图如下：

![1.jpg](https://github.com/liumapp/spring-boot-admin-in-docker/blob/master/pic/version2.1-1.jpg)

您也可以通过Docker的容器工具：kitmatic来查看容器的运行状态：

![2.jpg](https://github.com/liumapp/spring-boot-admin-in-docker/blob/master/pic/version2.1-2.jpg)
                        
### 1.3 配置Spring Security

详细操作请点击[这里](https://github.com/liumapp/spring-boot-admin-in-docker/wiki/1.3-%E9%85%8D%E7%BD%AESpring-Security)

首先请将项目切换到v1.3.0版本

    git checkout v1.3.0
    
在之前的版本中，我们并没有引入spring security

这意味着admin-server管理控制台随便是谁都可以登录，这在本地开放环境下是没有什么影响的

但是如果发布到线上呢？

所以接下来要实现的功能，就是给admin-server添加一个登录登出的界面跟按钮

相关效果如下图

![1.jpg](https://github.com/liumapp/spring-boot-admin-in-docker/blob/master/pic/version1.3.0-1.jpg)

![2.jpg](https://github.com/liumapp/spring-boot-admin-in-docker/blob/master/pic/version1.3.0-2.jpg)

首先我们要对admin-server引入spring security

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
随后进行配置：
    
    spring:
      security:
        user:
          name: "admin"
          password: "adminadmin"
    
    eureka:
        metadata-map:
          user.name: "admin"
          user.password: "adminadmin"
          
user.name与user.password便是登录的账号与密码

接下来修改启动类的代码：

    @Configuration
    @EnableAutoConfiguration
    @EnableDiscoveryClient
    @EnableAdminServer
    public class AdminServerMain {
    
        public static void main(String[] args) {
            SpringApplication.run(AdminServerMain.class, args);
        }
    
        @Configuration
        public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
    
            private final String adminContextPath;
    
            public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
                this.adminContextPath = adminServerProperties.getContextPath();
            }
    
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                // @formatter:off
                SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
                successHandler.setTargetUrlParameter("redirectTo");
                successHandler.setDefaultTargetUrl(adminContextPath + "/");
    
                http.authorizeRequests()
                        .antMatchers(adminContextPath + "/assets/**").permitAll()
                        .antMatchers(adminContextPath + "/login").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                        .logout().logoutUrl(adminContextPath + "/logout").and()
                        .httpBasic().and()
                        .csrf()
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringAntMatchers(
                                adminContextPath + "/instances",
                                adminContextPath + "/actuator/**",
                                adminContextPath + "/logout"
                        );
                // @formatter:on
            }
    
        }
    
    }         
    
启动类代码主要是参考spring boot admin官方手册上的

但是他们的官方手册有一个bug：

当你完全按照官方手册上来的做，你会发现点击"log out"按钮的时候，会报403异常

我附上的代码把这个bug解决掉了

接下来配置admin-client端，只需要在其注册到eureka的时候，附上admin-server配置的账号密码即可：

    eureka:
      instance:
        metadata-map:
          user.name: "admin"
          user.password: "adminadmin"
    
    
                      