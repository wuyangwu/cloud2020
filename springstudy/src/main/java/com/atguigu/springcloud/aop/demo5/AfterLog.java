package com.atguigu.springcloud.aop.demo5;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 16:21
 */
public class AfterLog implements AfterReturningAdvice {
    //返回值
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("执行了"+method.getName()+"返回结果为："+returnValue);
    }
}
