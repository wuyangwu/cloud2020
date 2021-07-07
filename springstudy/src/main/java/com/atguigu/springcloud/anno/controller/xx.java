package com.atguigu.springcloud.anno.controller;

import com.atguigu.springcloud.config.pojo.User;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 15:04
 */
public class xx {
    public static void main(String[] args) {
//        System.out.println(y());
        System.out.println(x());
    }
    public static int y(){
        User user = null;
        System.out.println(user.name);
        return 1;
    }

    public static  int x(){
        int x =1;
        try {
            User user = null;
            System.out.println(user.name);
            x = 2;

        }catch (Exception e){
            e.printStackTrace();
            x = 1;
        }finally {
            return x;
        }

    }
}
