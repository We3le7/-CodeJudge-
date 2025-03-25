package com.wes.wojbackendjudgeservice.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sun.org.apache.xml.internal.security.Init;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
@Slf4j
public class InitRabbitMq {
    public static void doInit(){
        try{
            ConnectionFactory factory=new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection=factory.newConnection();
            Channel channel=connection.createChannel();
            String EXCHANGE_NAME="code_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            String queueName="code_queue";
            channel.queueDeclare(queueName,true,false,false,null);
            channel.queueBind(queueName,EXCHANGE_NAME,"my_routingKey");
            log.info("rabbitmq init success");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        doInit();

    }
}
