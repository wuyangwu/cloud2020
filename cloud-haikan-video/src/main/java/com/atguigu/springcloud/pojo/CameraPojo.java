package com.atguigu.springcloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/26 15:29
 */
@Data
@AllArgsConstructor
public class CameraPojo implements Serializable {
    private static final long serialVersionUID = 8183688502930584159L;
    private String username;// 摄像头账号
    private String password;// 摄像头密码
    private String ip;// 摄像头ip
    private String cameraport; // 摄像头的端口
    private String channel;// 摄像头通道
    private String stream;// 摄像头码流
    private String rtsp;// rtsp地址
    private String rtmp;// rtmp地址
    private String url;// 播放地址
    private String starttime;// 回放开始时间
    private String endtime;// 回放结束时间
    private String opentime;// 打开时间
    private int count = 0;// 当前摄像头直播观看人数
    private String token;
    public CameraPojo(){

    }
}
