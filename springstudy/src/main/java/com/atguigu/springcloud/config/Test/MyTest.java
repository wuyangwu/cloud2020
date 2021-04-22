package com.atguigu.springcloud.config.Test;

import com.atguigu.springcloud.config.MyConfig;
import com.atguigu.springcloud.config.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/22 14:19
 */
public class MyTest {
    @Test
    public void test(){
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        User use = (User) context.getBean("getUser");
        System.out.println(use.name);
    }
}
