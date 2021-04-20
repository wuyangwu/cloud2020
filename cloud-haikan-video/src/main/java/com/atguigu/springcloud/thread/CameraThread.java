package com.atguigu.springcloud.thread;

import com.atguigu.springcloud.cache.CacheCamera;
import com.atguigu.springcloud.controller.CameraInit;
import com.atguigu.springcloud.pojo.CameraPojo;
import com.atguigu.springcloud.pojo.Config;
import com.atguigu.springcloud.push.CameraPush;

import java.lang.reflect.Executable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/26 17:15
 */
public class CameraThread {

    public static class myrun implements Runnable {

        public static ExecutorService es = Executors.newCachedThreadPool();

        private CameraInit cameraInit;

        private Config config;

        public myrun(){

        }
        public myrun(CameraInit cameraInit,Config config){
            this.cameraInit = cameraInit;
            this.config = config;
        }

        @Override
        public void run() {
            CameraPush cameraPush = new CameraPush(cameraInit,config);
            cameraPush.push();
        }
    }

}
