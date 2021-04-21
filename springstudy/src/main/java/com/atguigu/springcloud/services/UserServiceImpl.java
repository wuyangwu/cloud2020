package com.atguigu.springcloud.services;

import com.atguigu.springcloud.dao.UserDao;
import com.atguigu.springcloud.dao.UserDaoImpl;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/20 13:48
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao =null;

    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public void getUser() {
        userDao.getUser();
    }
}
