package com.atguigu.springcloud.commons;

import com.atguigu.springcloud.controller.CameraInit;
import com.sun.jna.NativeLong;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/25 16:19
 */
@Slf4j
public class YunTaiController {
    /*************************************************
     函数名:    PTZControlAll
     函数描述:	云台控制函数
     输入参数:
     lRealHandle: 预览句柄
     iPTZCommand: PTZ控制命令
     iStop: 开始或是停止操作
     输出参数:
     返回值:
     *************************************************/
    public static void PTZControlAll(NativeLong lRealHandle, int iPTZCommand, int iStop){
        int iSpeed = 0;
        if(lRealHandle.intValue()  >= 0){
            boolean ret;
            if (iSpeed >= 1)//有速度的ptz
            {

                ret = CameraInit.hCNetSDK.NET_DVR_PTZControlWithSpeed(lRealHandle, iPTZCommand, iStop, iSpeed);
                if (!ret)
                {
                    log.error("云台控制失败");
//                    JOptionPane.showMessageDialog(this, "云台控制失败");
                    return;
                }
            } else//速度为默认时
            {
                ret = CameraInit.hCNetSDK.NET_DVR_PTZControl(lRealHandle, iPTZCommand, iStop);
                if (!ret)
                {
                    log.error("云台控制失败");
//                    JOptionPane.showMessageDialog(this, "云台控制失败");
                    return;
                }
            }
        }else {
            log.error("摄像头没有注册，云台无法控制");
        }

    }
}
