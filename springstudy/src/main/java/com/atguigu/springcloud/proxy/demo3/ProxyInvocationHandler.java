package com.atguigu.springcloud.proxy.demo3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 14:00
 */
//等会儿我们会用这类，自动生成代理类
public class ProxyInvocationHandler implements InvocationHandler {

    //被代理的接口
    private Rent rent;

    public void setRent(Rent rent){
        this.rent = rent;
    }

    //生成得到代理类
    public Object getProxy(){
      return   Proxy.newProxyInstance(this.getClass().getClassLoader(),rent.getClass().getInterfaces(),this);
    }
    //处理代理实例，并返回结果
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.seeHouse();
        Object rest = method.invoke(rent,args);
        this.fare();
        return rest;
    }

    //
    public void seeHouse(){
        System.out.println("房东看房子");
    }

    public void fare(){
        System.out.println("价格100元");
    }
}
