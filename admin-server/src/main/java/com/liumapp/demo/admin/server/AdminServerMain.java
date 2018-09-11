package com.liumapp.demo.admin.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author liumapp
 * @file AdminServerMain.java
 * @email liumapp.com@gmail.com
 * @homepage http://www.liumapp.com
 * @date 2018/8/10
 */
@Configuration
@EnableAutoConfiguration
@EnableAdminServer
public class AdminServerMain {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerMain.class, args);
    }
}