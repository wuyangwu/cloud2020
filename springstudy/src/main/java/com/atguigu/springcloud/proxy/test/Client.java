package com.atguigu.springcloud.proxy.test;

import com.atguigu.springcloud.proxy.demo01.Host;
import com.atguigu.springcloud.proxy.demo01.Proxy;
import com.atguigu.springcloud.proxy.demo3.ProxyInvocationHandler;
import com.atguigu.springcloud.proxy.demo3.Rent;
import org.junit.jupiter.api.Test;


/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/22 17:04
 */
public class Client {
    @Test
    public void test(){
        Host host = new Host();
        //代理
        Proxy proxy = new Proxy(host);
        proxy.rent();
        proxy.seeHouse();
    }

    @Test
    public void test1(){
        // 真实角色
        Host host = new Host();
        //代理角色
        ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler();
        proxyInvocationHandler.setRent(host);
        Rent rent = (Rent) proxyInvocationHandler.getProxy();
        rent.rent();
    }
}
