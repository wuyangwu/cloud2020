package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/21 9:27
 */
@SpringBootApplication
@EnableEurekaServer
public class EurrkaMain7002 {
    public static void main(String[] args) {
        SpringApplication.run(EurrkaMain7002.class,args);
    }
}
