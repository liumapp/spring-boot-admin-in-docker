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
    
    
                      