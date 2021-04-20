package com.atguigu.springcloud.push;

import com.atguigu.springcloud.controller.CameraInit;
import com.atguigu.springcloud.pojo.Config;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.bytedeco.ffmpeg.global.avcodec.av_packet_unref;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/28 15:13
 */
@Slf4j
public class CameraPush {

    private Config config;

    private CameraInit cameraInit;

    private FFmpegFrameRecorder recorder; // 解码器  帧录制器/推流器（FrameRecorder） 从帧收集器中获取最新的流推
    private FFmpegFrameGrabber grabber;     // 采集器 帧抓取器（FrameGrabber） 帧收集器从流中抓取最新的帧
    private int err_index = 0;// 推流过程中出现错误的次数
    private int exitcode = 0;// 退出状态码：0-正常退出;1-手动中断;
    private double framerate = 0;// 帧率

    public CameraPush(){

    }

    public CameraPush(CameraInit cameraInit,Config config){
       this.cameraInit =  cameraInit;
       this.config = config;
    }


    /**
     * @Description: 资源释放
     * @Author:  yangStudent
     * @CreateDate: 2021/1/28 15:15
     */
    private void release() {
        try {
            grabber.stop();
            grabber.close();
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void push(){
       try{
           avutil.av_log_set_level(avutil.AV_LOG_INFO);
           FFmpegLogCallback.set();
           // 获取视频源
           grabber = new FFmpegFrameGrabber(cameraInit.pojo.getRtsp());  // 绑定拉流的ip new初始化及初始化设置-->start（用于启动一些初始化和解析操作）-->循环调用grab()获取音视频-->stop（销毁所有对象，回收内存）
           grabber.setOption("rtsp_transport", "tcp");
           // 设置采集器构造超时时间
           grabber.setOption("stimeout", "2000000");
           if ("sub".equals(cameraInit.pojo.getStream())) {
               grabber.start(config.getSub_code());
           } else if ("main".equals(cameraInit.pojo.getStream())) {
               grabber.start(config.getMain_code());
           } else {
               grabber.start(config.getMain_code());
           }
           // 部分监控设备流信息里携带的帧率为9000，如出现此问题，会导致dts、pts时间戳计算失败，播放器无法播放，故出现错误的帧率时，默认为25帧
           if (grabber.getFrameRate() > 0 && grabber.getFrameRate() < 100) {
               framerate = grabber.getFrameRate();
           } else {
               framerate = 25.0;
           }
           int width = grabber.getImageWidth();
           int height = grabber.getImageHeight();
           // 若视频像素值为0，说明拉流异常，程序结束
           if (width == 0 && height == 0) {
               log.error(cameraInit.pojo.getRtsp() + "  拉流异常！");
               grabber.stop();
               grabber.close();
               release();
               return;
           }
           recorder = new FFmpegFrameRecorder(cameraInit.pojo.getRtmp(), grabber.getImageWidth(), grabber.getImageHeight());  //
           recorder.setInterleaved(true);
           // 关键帧间隔，一般与帧率相同或者是视频帧率的两倍
           recorder.setGopSize((int) framerate * 2);
           // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏)
           recorder.setFrameRate(framerate);
           // 设置比特率
           recorder.setVideoBitrate(grabber.getVideoBitrate());
           // 封装flv格式 录制视频的格式
           recorder.setFormat("flv");
           // h264编/解码器
           recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
           recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
           Map<String, String> videoOption = new HashMap<>();
           // 该参数用于降低延迟
           videoOption.put("tune", "zerolatency");
           /**
            ** 权衡quality(视频质量)和encode speed(编码速度) values(值)： *
            * ultrafast(终极快),superfast(超级快), veryfast(非常快), faster(很快), fast(快), *
            * medium(中等), slow(慢), slower(很慢), veryslow(非常慢) *
            * ultrafast(终极快)提供最少的压缩（低编码器CPU）和最大的视频流大小；而veryslow(非常慢)提供最佳的压缩（高编码器CPU）的同时降低视频流的大小
            */
           videoOption.put("preset", "ultrafast");
           // 画面质量参数，0~51；18~28是一个合理范围
           videoOption.put("crf", "28");
           recorder.setOptions(videoOption); //传入的设置参数
           AVFormatContext fc = grabber.getFormatContext();
           recorder.start(fc);  // 开始
//			recorder.start();  // 开始
           log.info("开始推流 设备信息：[ip:" + cameraInit.pojo.getIp() + " channel:" + cameraInit.pojo.getChannel() + " stream:"
                   + cameraInit.pojo.getStream() + " starttime:" + cameraInit.pojo.getStarttime() + " endtime:" + cameraInit.pojo.getEndtime()
                   + " rtsp:" + cameraInit.pojo.getRtsp() + " url:" + cameraInit.pojo.getRtmp() + "]");
           // 释放探测时缓存下来的数据帧，避免pts初始值不为0导致画面延时
           grabber.flush();
           AVPacket pkt = null;
           long dts = 0;
           long pts = 0;
           int timebase = 0;
           for (int no_frame_index = 0; no_frame_index < 5 && err_index < 5;) { // 循环一直推流吐过出现10次空包现象或者就
               long time1 = System.currentTimeMillis();
               if (exitcode == 1) {
                   break;
               }
               pkt = grabber.grabPacket(); // 获取没有经过解码的音/视频帧
               if (pkt == null || pkt.size() == 0 || pkt.data() == null) {
                   // 空包记录次数跳过
                   log.error("JavaCV 出现空包 设备信息：[ip:" + cameraInit.pojo.getIp() + " channel:" + cameraInit.pojo.getChannel() + " stream:"
                           + cameraInit.pojo.getStream() + " starttime:" + cameraInit.pojo.getStarttime() + " endtime:" + " rtsp:"
                           + cameraInit.pojo.getRtsp() + cameraInit.pojo.getEndtime() + " url:" + cameraInit.pojo.getRtmp() + "]");
                   no_frame_index++;
                   continue;
               }
               // 过滤音频
               if (pkt.stream_index() == 1) {
                   av_packet_unref(pkt);
                   continue;
               }
               // 矫正sdk回调数据的dts，pts每次不从0开始累加所导致的播放器无法续播问题
               pkt.pts(pts);
               pkt.dts(dts);
               err_index += (recorder.recordPacket(pkt) ? 0 : 1); //如果失败自增加1
               // pts,dts累加
               timebase = grabber.getFormatContext().streams(pkt.stream_index()).time_base().den();
               pts += timebase / (int) framerate;
               dts += timebase / (int) framerate;
               // 将缓存空间的引用计数-1，并将Packet中的其他字段设为初始值。如果引用计数为0，自动的释放缓存空间。
               av_packet_unref(pkt);
               long endtime = System.currentTimeMillis();
               if ((long) (1000 /framerate) - (endtime - time1) > 0) {
                   Thread.sleep((long) (1000 / framerate) - (endtime - time1));
               }
           }
       }catch (Exception e){
            log.error("推流异常信息：{}",e.getMessage());
       }finally {
            release();
           log.info("推流结束 设备信息：[ip:" + cameraInit.pojo.getIp() + " channel:" + cameraInit.pojo.getChannel() + " stream:"
                   + cameraInit.pojo.getStream() + " starttime:" + cameraInit.pojo.getStarttime() + " endtime:" + cameraInit.pojo.getEndtime()
                   + " rtsp:" + cameraInit.pojo.getRtsp() + " url:" + cameraInit.pojo.getUrl() + "]");
       }
    }
}
