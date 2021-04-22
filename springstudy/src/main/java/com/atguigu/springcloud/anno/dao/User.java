package com.atguigu.springcloud.anno.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/22 10:12
 */

@Component
public class User {

    //  相当于<property name="name" value="wuyang" />
    @Value("wuyang")
    public String name;
}
