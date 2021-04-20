package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/20 13:31
 */
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced  //开启负载均衡  默认是轮询
    public RestTemplate getRestTemplate(){
        return  new RestTemplate();
    }
}
