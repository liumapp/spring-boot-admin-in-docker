package com.liumapp.demo.admin.eureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

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

}
