package com.atguigu.springcloud.aop.demo5;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 16:45
 */
public class test {
    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        //动态代理代理的是接口
        UserService userService1 = context.getBean("userService1", UserService.class);
        userService1.add();
    }
}
