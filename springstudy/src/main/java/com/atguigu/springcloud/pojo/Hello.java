package com.atguigu.springcloud.pojo;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/20 14:50
 */
public class Hello {

    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "Hello{" +
                "str='" + str + '\'' +
                '}';
    }
}
