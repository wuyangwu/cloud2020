package com.atguigu.springcloud.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/20 13:45
 */
public class UserDaoImpl implements UserDao {
    @Override
    public void getUser() {
        System.out.println("我是用户");
    }
}
