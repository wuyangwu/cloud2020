package com.atguigu.springcloud.cache;

import com.atguigu.springcloud.controller.CameraInit;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/27 10:37
 *
 * 缓存摄像头的加载的一些类
 *
 */
@Slf4j
@Component
public class CacheCamera {

   private static  Map<String, CameraInit> cameraInfo = new HashMap<>();

   //值是否存在
    public boolean cameraInfoExsit(String key){
        return cameraInfo.containsKey(key);
    }

   // 存入值
   public boolean cameraInfoPut(String cameraurl,CameraInit cameraInit){
       if (!cameraInfoExsit(cameraurl)){
           cameraInfo.put(cameraurl,cameraInit);
            return true;
       }
       log.info("值已经在缓存中存在");
       return false;
    }

    // 取值
    public  CameraInit cameraInfoGet(String key){
        if (cameraInfo.containsKey(key)){
            return cameraInfo.get(key);
        }
//        log.info("缓存中不存在改值");
        return null;
    }

    // 获取缓存所有值
    public Map<String,CameraInit> cameraInfoGetAll(){
       if(cameraInfo.size()>0){
           return cameraInfo;
       }
       return null;
    }

    //清除缓存种所有内容
    public boolean clearAll(){
       if (cameraInfo.size()>0){
           cameraInfo.clear();
       }
       return true;
    }

    public boolean remove(String key){
        if(cameraInfo.containsKey(key)){
            cameraInfo.remove(key);
        }
        return true;
    }

}
