package com.liumapp.demo.admin.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liumapp
 * @file AdminClientMain.java
 * @email liumapp.com@gmail.com
 * @homepage http://www.liumapp.com
 * @date 2018/8/10
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class AdminClientMain {

    public static void main (String[] args) {
        SpringApplication.run(AdminClientMain.class, args);
    }

}
