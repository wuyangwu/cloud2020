package com.atguigu.springcloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.springcloud.cache.CacheCamera;
import com.atguigu.springcloud.commons.HCNetSDK;
import com.atguigu.springcloud.commons.YunTaiController;
import com.atguigu.springcloud.pojo.CameraPojo;
import com.atguigu.springcloud.pojo.Config;
import com.atguigu.springcloud.thread.CameraThread;
import com.atguigu.springcloud.util.Utils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/20 15:04
 * spring 默认是单例模式controller默认是单例的，不要使用非静态的成员变量，否则会发生数据逻辑混乱。正因为单例所以不是线程安全的
 */
@Slf4j
@RestController
@Scope("prototype") // 通过Scope修改变成多例模式
public class VideoController {

    @Autowired
    private CameraInit cameraInit;

    @Autowired
    private CacheCamera cacheCamera;

    @Autowired
    private Config config;

    private int iChannelNum;

    public VideoController(){
    }

    @PostMapping(value = "/register")
    public void regesiter(@RequestBody CameraPojo pojo){
        String[] isNullArr = { "ip", "username", "password", "stream" };
        JSONObject cameraJson = JSONObject.parseObject(JSONObject.toJSON(pojo).toString());
        if (!Utils.isNullParameters(cameraJson, isNullArr)) {
            log.error("输入的参数值不完整无法注册");
            return;
        }
        if(!Utils.isTrueIp(pojo.getIp())){
            log.info("ip地址格式错误");
            return;
        }

        if(cameraInit.lUserID.longValue() > -1||cacheCamera.cameraInfoExsit(pojo.getIp())){
            log.info("摄像头已经注册，先注销");
            return;
        }
        cameraInit.m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int deviceport = 8000;
        cameraInit.lUserID = CameraInit.hCNetSDK.NET_DVR_Login_V30(pojo.getIp(),(short)deviceport,"admin","abcd1234",cameraInit.m_strDeviceInfo);// 设备注册
        long userID = cameraInit.lUserID.longValue();
        if(userID == -1){
            log.info("=============注册失败,密码或者账户输入错误");
        }else {
            cameraInit.pojo.setIp(pojo.getIp());
            cameraInit.pojo.setUsername(pojo.getUsername());
            cameraInit.pojo.setPassword(pojo.getPassword());
            cameraInit.pojo.setStream(pojo.getStream());
           getDevice();
            log.info("==============注册成功建立通道数,获取通道号:{}",iChannelNum);
        }
    }

    // 获取通道参数
    private void getDevice(){
        IntByReference ibrBytesReturned = new IntByReference(0);//获取IP接入配置参数
        boolean bRet = false;
        cameraInit.m_strIpparaCfg = new HCNetSDK.NET_DVR_IPPARACFG();
        cameraInit.m_strIpparaCfg.write();
        Pointer lpIpParaConfig = cameraInit.m_strIpparaCfg.getPointer();
        // 获取通道参数
        iChannelNum = cameraInit.m_strDeviceInfo.byStartChan;
        cameraInit.pojo.setChannel(String.valueOf(iChannelNum+32));
        cameraInit.m_strIpparaCfg.read();
        jButtonRealPlayActionPerformed();
    }

    @GetMapping("/Cameras/LiveVideo")
    public String LiveVideo(@RequestParam("cameraurl") String cameraurl){
        if(!cacheCamera.cameraInfoExsit(cameraurl)){
            log.info("缓存中找不到这个设备");
            return "您的设备没有注册";
        }
        CameraInit cameraInit =cacheCamera.cameraInfoGet(cameraurl);
        String token = UUID.randomUUID().toString();
        String rtsp = "";
        String rtmp = "";
        String Ip =Utils.IpConvert(cameraInit.pojo.getIp());
        rtsp = "rtsp://"+cameraInit.pojo.getUsername()+":"+cameraInit.pojo.getPassword()+"@"+Ip+":554/h264/ch"+cameraInit.pojo.getChannel()+"/"+cameraInit.pojo.getStream()+"/av_stream";
        rtmp = "rtmp://"+Utils.IpConvert(config.getPush_host())+":"+config.getPush_port()+"/live/"+token;
        cameraInit.pojo.setRtmp(rtmp);
        cameraInit.pojo.setUrl(rtmp);
        cameraInit.pojo.setRtsp(rtsp);
        cameraInit.pojo.setCount(cameraInit.pojo.getCount()+1);
        // 解决ip输入错误时，grabber.start();出现阻塞无法释放grabber而导致后续推流无法进行；提前进行ip连通性测试，防止后面推流出现堵塞
        if(!ipConnectTest(cameraInit)){
            return "您输入的ip异常";
        }
        CameraThread.myrun cameraThread = new CameraThread.myrun(cameraInit,config);
        CameraThread.myrun.es.execute(cameraThread);
        return cameraInit.pojo.getUrl();
    }

    private boolean ipConnectTest(CameraInit cameraInit){
        Socket rtspsocket = new Socket();
        Socket rtmpsocket = new Socket();
        try {
            rtspsocket.connect(new InetSocketAddress(cameraInit.pojo.getIp(), 554), 1000);
        }catch (IOException e){
            log.error("异常：{}",e.getMessage());
            log.error("与拉流IP：   " + cameraInit.pojo.getIp()+"   端口：  554"+ " 建立TCP连接失败");
            return false;
        }
        try {
            rtspsocket.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }finally {
            log.info("ip和端口能通过连接测试");
        }
        try {
            rtmpsocket.connect(new InetSocketAddress(Utils.IpConvert(config.getPush_host()),
                    Integer.parseInt(config.getPush_port())), 1000);
        } catch (IOException e) {
            log.error("异常：{}",e.getMessage());
            log.error("与推流IP：   " +config.getPush_host()+"   端口：   "+config.getPush_port()+" 建立TCP连接失败！");
            return false;
        }
        try {
            rtmpsocket.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }finally {
            log.info("ip和端口能通过连接测试");
        }
        return true;
    }

    private void jButtonRealPlayActionPerformed(){
        if (cameraInit.lUserID.intValue() == -1)
        {
            log.info("请先注册");
            return;
        }
        cameraInit.m_strClientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();
        cameraInit.m_strClientInfo.lChannel = new NativeLong(iChannelNum);
        cameraInit.lPreviewHandle = CameraInit.hCNetSDK.NET_DVR_RealPlay_V30(cameraInit.lUserID,
        cameraInit.m_strClientInfo, null, null, true);
        cacheCamera.cameraInfoPut(cameraInit.pojo.getIp(),cameraInit); // 存入cacheCamera缓存种，一个url注册一次就可以，避免反复注册

    }

    // 左上
    @GetMapping(value = "/leftup")
    public void PTZLeftUpController(@RequestParam("cameraurl") String cameraurl){
        if(cacheCamera.cameraInfoExsit(cameraurl)){
            CameraInit cameraInit = cacheCamera.cameraInfoGet(cameraurl);
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.UP_LEFT, 0);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error("线程睡眠异常");
            }
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.UP_LEFT, 1);
        }else{
            log.info("没有注册");
        }
    }
    //右下
    @GetMapping(value = "/rightdown")
    public void PTZRightDownController(@RequestParam("cameraurl") String cameraurl){
        if(cacheCamera.cameraInfoExsit(cameraurl)){
            CameraInit cameraInit = cacheCamera.cameraInfoGet(cameraurl);
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.DOWN_RIGHT, 0);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error("线程睡眠异常");
            }
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.DOWN_RIGHT, 1);
        }else{
            log.info("没有注册");
        }
    }
    // 上
   @GetMapping(value = "/up")
    public void PTZUpController(@RequestParam("cameraurl") String cameraurl){
        if(cacheCamera.cameraInfoExsit(cameraurl)){
            CameraInit cameraInit = cacheCamera.cameraInfoGet(cameraurl);
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.TILT_UP, 0);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error("线程睡眠异常");
            }
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.TILT_UP, 1);
        }else{
            log.info("没有注册");
        }
   }
   //下
   @GetMapping(value = "down")
    public void PTZDownController(@RequestParam("cameraurl") String cameraurl){
       if(cacheCamera.cameraInfoExsit(cameraurl)){
           CameraInit cameraInit = cacheCamera.cameraInfoGet(cameraurl);
           YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.TILT_DOWN, 0);
           try {
               TimeUnit.MILLISECONDS.sleep(100);
           } catch (InterruptedException e) {
               log.error("线程睡眠异常");
           }
           YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.TILT_DOWN, 1);
       }else{
           log.info("没有注册");
       }
    }
    //右上
    @GetMapping(value = "rightup")
    public void PTZRightUpController(@RequestParam("cameraurl") String cameraurl){
        if(cacheCamera.cameraInfoExsit(cameraurl)){
            CameraInit cameraInit = cacheCamera.cameraInfoGet(cameraurl);
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.UP_RIGHT, 0);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error("线程睡眠异常");
            }
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.UP_RIGHT, 1);
        }else{
            log.info("没有注册");
        }
    }
    //左下
    @GetMapping(value = "leftdown")
    public void PTZLeftDownController(@RequestParam("cameraurl") String cameraurl){
        if(cacheCamera.cameraInfoExsit(cameraurl)){
            CameraInit cameraInit = cacheCamera.cameraInfoGet(cameraurl);
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.DOWN_LEFT, 0);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error("线程睡眠异常");
            }
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.DOWN_LEFT, 1);
        }else{
            log.info("没有注册");
        }
    }
    //左
    @GetMapping(value = "left")
    public void PTZLeftController(@RequestParam("cameraurl") String cameraurl){
        if(cacheCamera.cameraInfoExsit(cameraurl)){
            CameraInit cameraInit = cacheCamera.cameraInfoGet(cameraurl);
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.PAN_LEFT, 0);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error("线程睡眠异常");
            }
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.PAN_LEFT, 1);
        }else{
            log.info("没有注册");
        }
    }
    //右
    @GetMapping(value = "right")
    public void PTZRightController(@RequestParam("cameraurl") String cameraurl){
        if(cacheCamera.cameraInfoExsit(cameraurl)){
            CameraInit cameraInit = cacheCamera.cameraInfoGet(cameraurl);
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.PAN_RIGHT, 0);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error("线程睡眠异常");
            }
            YunTaiController.PTZControlAll(cameraInit.lPreviewHandle,HCNetSDK.PAN_RIGHT, 1);
        }else{
            log.info("没有注册");
        }
    }
}
