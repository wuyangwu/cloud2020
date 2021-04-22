package com.atguigu.springcloud.config.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/22 13:53
 */
@Component
public class User {
    @Value("wuyang")
    public String name;
}
