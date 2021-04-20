package com.atguigu.springcloud;

import com.atguigu.springcloud.pojo.Config;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/20 15:00
 */
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(Config.class)
public class VideoMain {
    public static void main(String[] args) {
        try {
            FFmpegFrameGrabber.tryLoad();
            FFmpegFrameRecorder.tryLoad();
            log.info("==================海康sdk初始化成功");
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            SpringApplication.run(VideoMain.class,args);
            log.info("==================Spring启动成功");
        }

    }
}
