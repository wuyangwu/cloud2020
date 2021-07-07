package com.atguigu.springcloud.aop.diy;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 22:09
 */
public class DiyPointCut {
    public void before(){
        System.out.println("方法执行前");
    }
    public void after(){
        System.out.println("方法执行后");
    }
}
