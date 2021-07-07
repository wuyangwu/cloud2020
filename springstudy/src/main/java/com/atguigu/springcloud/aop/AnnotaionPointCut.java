package com.atguigu.springcloud.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 22:38
 */
@Aspect //标注这个类是一个切面
public class AnnotaionPointCut {
    @Before("execution(* com.atguigu.springcloud.aop.demo5.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("=====被执行之前=====");
    }

    @After("execution(* com.atguigu.springcloud.aop.demo5.UserServiceImpl.*(..))")
    public void after(){
        System.out.println("====被执行之后=====");
    }

    // 在环绕增强种，我们可以给定一个参数，代表我们要获取处理切入的点
    @Around("execution(* com.atguigu.springcloud.aop.demo5.UserServiceImpl.*(..))")
    public void around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕前");

        Signature signature = jp.getSignature();//获得签名
        System.out.println("signature: "+signature);
        // 执行方法
        Object proceed = jp.proceed();
        System.out.println("环绕后");
        System.out.println();
    }
}

