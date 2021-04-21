package com.atguigu.springcloud.di;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/21 10:30
 */
public class Address {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Adress{" +
                "name='" + name + '\'' +
                '}';
    }
}
