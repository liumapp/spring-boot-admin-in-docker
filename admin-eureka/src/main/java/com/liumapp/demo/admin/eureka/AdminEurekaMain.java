package com.liumapp.demo.admin.eureka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author liumapp
 * @file AdminEurekaMain.java
 * @email liumapp.com@gmail.com
 * @homepage http://www.liumapp.com
 * @date 2018/8/10
 */
@SpringBootApplication
@EnableEurekaServer
public class AdminEurekaMain {

    public static void main (String[] args) {
        SpringApplication.run(AdminEurekaMain.class, args);
    }

    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${spring.security.name}")
        private String username;

        @Value("${spring.security.password}")
        private String password;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
            http.csrf().disable();
            //注意：为了可以使用 http://${user}:${password}@${host}:${port}/eureka/ 这种方式登录,所以必须是httpBasic,如果是form方式,不能使用url格式登录
            http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                    //admin
                    .withUser(username).password(password).roles("EUREKA-CLIENT").and()
                    //eureka-security-client
                    .withUser("eureka-security-client").password("eureka-security-client").roles("EUREKA-CLIENT")
            ;
        }
    }

}
