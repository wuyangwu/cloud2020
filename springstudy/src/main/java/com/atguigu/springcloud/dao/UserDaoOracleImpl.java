package com.atguigu.springcloud.dao;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/20 14:34
 */
public class UserDaoOracleImpl implements UserDao {
    @Override
    public void getUser() {
        System.out.println("我是Oracle");
    }
}
