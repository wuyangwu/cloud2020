package com.atguigu.springcloud.services;

import com.atguigu.springcloud.dao.UserDao;

public interface UserService {
    void getUser();

    void setUserDao(UserDao userDao);
}
