package com.wes.wojbackenduserservice;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.wes.wojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
@ComponentScan("com.wes")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.wes.wojbackendserviceclient.service")
@EnableKnife4j
public class WojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WojBackendUserServiceApplication.class, args);
    }

}
