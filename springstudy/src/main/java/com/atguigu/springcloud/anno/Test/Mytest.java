package com.atguigu.springcloud.anno.Test;

import com.atguigu.springcloud.anno.dao.User;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/22 10:34
 */
public class Mytest {

    private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

   @Test
    public void test(){
       User user = context.getBean("user", User.class);
       System.out.println(user.name);
   }
}
