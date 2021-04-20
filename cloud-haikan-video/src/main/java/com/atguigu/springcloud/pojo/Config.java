package com.atguigu.springcloud.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/26 15:06
 */

@Data
@Component
@ConfigurationProperties(prefix = "config")
public class Config {

    private String keepalive; // 保活时长（分钟）

    private String push_host; // 推送地址

    private String host_extra; // 额外地址

    private String push_port; // 推送端口

    private String main_code; // 主码流最大码率

    private String sub_code; // 主码流最大码率


}
