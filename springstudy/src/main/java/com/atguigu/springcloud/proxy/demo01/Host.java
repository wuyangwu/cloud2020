package com.atguigu.springcloud.proxy.demo01;

import com.atguigu.springcloud.proxy.demo3.Rent;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/22 16:45
 */
public class Host implements Rent {
    @Override
    public void rent() {
        System.out.println("房东要出租房子！");
    }
}
