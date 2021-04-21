package com.atguigu.springcloud.dao;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/21 9:27
 */
public class user {

    private String name;

    public user(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "user{" +
                "name='" + name + '\'' +
                '}';
    }
}
