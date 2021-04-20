package com.atguigu.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/21 15:04
 */
@Configuration
public class MySelfRule {


    @Bean
    public IRule myRule(){
        return new RandomRule();//定义为随机
    }
}

