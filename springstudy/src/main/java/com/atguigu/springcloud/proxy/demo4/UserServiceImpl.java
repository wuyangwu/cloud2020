package com.atguigu.springcloud.proxy.demo4;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 9:50
 */
public class UserServiceImpl implements UserService {
    @Override
    public void add() {
        System.out.println("增加了一个用户");
    }

    @Override
    public void delete() {
        System.out.println("删除一个业务");
    }

    @Override
    public void update() {
        System.out.println("更新一个用户");
    }

    @Override
    public void query() {
        System.out.println("查询一个业务");
    }
}
