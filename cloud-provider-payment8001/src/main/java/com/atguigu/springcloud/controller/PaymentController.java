package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import com.atguigu.springcloud.test.MapTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/20 11:20
 */
@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String servePort;

    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/payment/test")
    public void test(){
        log.info( MapTest.map.get("1"));
    }

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
            int result = paymentService.create(payment);
            log.info("======插入结果:"+result);
            if(result > 0){
                return new CommonResult(200,"插入数据库成功,serverport"+servePort,result);
            }else {
                return new CommonResult(444,"插入数据库失败",null);
            }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult create(@PathVariable("id") long id){
        Payment payment = paymentService.getPaymentById(id);
        if(payment != null){
            return new CommonResult<Payment>(200,"查询成功,serverport"+servePort,payment);
        }else {
            return new CommonResult(444,"没有对应记录",null);
        }
    }

    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String>  services = discoveryClient.getServices();
        for(int i = 0; i< services.size();i++){
           log.info("========element:"+services.get(i));
        }
      List<ServiceInstance> serviceInstances= discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance element:serviceInstances){
            log.info(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }

        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return servePort;
    }

}
