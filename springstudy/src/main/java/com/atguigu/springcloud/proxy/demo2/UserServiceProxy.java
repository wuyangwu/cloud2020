package com.atguigu.springcloud.proxy.demo2;

import com.atguigu.springcloud.proxy.demo2.UserServiceImpl;
/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 10:05
 */
public class UserServiceProxy implements UserService {

    public UserServiceImpl userService;

    public void setUserService(UserServiceImpl userService){
        this.userService = userService;
    }


    @Override
    public void add() {
        log("使用add方法");
        userService.add();
    }

    @Override
    public void delete() {
        log("使用删除方法");
        userService.delete();
    }

    @Override
    public void update() {
        log("使用更新方法");
        userService.update();
    }

    @Override
    public void query() {
        log("使用查询方法");
        userService.query();
    }

    //日志方法
    public void log(String msg){
        System.out.println("使用了"+msg+"方法");
    }
}
