package com.atguigu.springcloud.config;

import com.atguigu.springcloud.config.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/22 13:56
 */
@Configuration//这个也会被Spring容器托管，注册到容器中，因为他本身就是一个@Component，@Configuration代表这是一个配置类，就和我们之前看到的beans。xml
@ComponentScan("com.atguigu.springcloud.config.pojo") //扫描
@Import(MyConfig2.class) // 2个配置合并成一个类
public class MyConfig {

    //注册一个bean，就相当于我们之前写的一个bean标签
    //这个方法的名字，就相当于bean标签中的id属性
    //这个方法的返回值，就相当于bean标签中的class属性
    @Bean
    public User getUser(){
        return new User(); // 就是返回要注入到bean的对象
    }
}
