package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.commons.HCNetSDK;
import com.atguigu.springcloud.pojo.CameraPojo;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/26 18:14
 * 该类初始化摄像头的加载
 */
@Slf4j
@Component
@Scope("prototype")
public class CameraInit {
   public static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE; // 加载控制云台和设备的驱动
    boolean bRealPlay;//是否在预览.
    public NativeLong lUserID;//用户句柄
    public NativeLong lPreviewHandle;//预览句柄
    public HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;//设备信息
    public HCNetSDK.NET_DVR_IPPARACFG  m_strIpparaCfg;//IP参数
    public HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo;//用户参数
    public CameraPojo pojo;  //

    static {
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc!= true){
            log.error("摄像头类初始化加载失败");
        }else{
            log.info("摄像头类初始化加载成功");
        }
    }

    CameraInit(){
        lUserID = new NativeLong(-1);
        lPreviewHandle = new NativeLong(-1);
        pojo = new CameraPojo();
    }
}
