package com.atguigu.springcloud.aop.demo5;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 16:14
 */
public class Logs implements MethodBeforeAdvice {
    // method:要执行的目标对象的方法
    // args:参数
    // target 目标对象
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(target.getClass().getName()+"的"+method.getName()+"被执行了");
    }
}
