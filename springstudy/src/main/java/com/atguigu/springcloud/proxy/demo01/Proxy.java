package com.atguigu.springcloud.proxy.demo01;

import com.atguigu.springcloud.proxy.demo3.Rent;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/22 17:05
 */
public class Proxy implements Rent {

    private Host host;

    public Proxy(){

    }

    public Proxy(Host host){
        this.host = host;
    }

    @Override
    public void rent() {
        host.rent();
    }

    //看房
    public void seeHouse(){
        System.out.println("中介带你看房子");
    }

    //收中介费
    public void fare(){
        System.out.println("收中介费");
    }


}
