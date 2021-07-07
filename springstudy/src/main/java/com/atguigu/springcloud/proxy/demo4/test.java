package com.atguigu.springcloud.proxy.demo4;

import org.junit.Test;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 15:26
 */
public class test {
    @Test
    public void test(){
        //真实角色
        UserServiceImpl userService = new UserServiceImpl();
        //代理角色，不存在
        ProxyInvocationHandler pih = new ProxyInvocationHandler();
        pih.setTarget(userService);
        UserService proxy = (UserService) pih.getProxy();
        proxy.delete();

    }
}
