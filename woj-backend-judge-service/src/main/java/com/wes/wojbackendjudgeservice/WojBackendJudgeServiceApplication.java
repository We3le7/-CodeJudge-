package com.wes.wojbackendjudgeservice;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.wes.wojbackendjudgeservice.rabbitMq.InitRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.wes")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.wes.wojbackendserviceclient.service")
@EnableKnife4j
public class WojBackendJudgeServiceApplication {


    public static void main(String[] args) {

        InitRabbitMq.doInit();
        SpringApplication.run(WojBackendJudgeServiceApplication.class, args);
    }

}
