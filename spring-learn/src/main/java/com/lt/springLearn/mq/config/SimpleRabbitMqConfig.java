package com.lt.springLearn.mq.config;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.simple.SimpleReciver;
import com.lt.springLearn.mq.simple.SimpleSender;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 简单模式是最简单的消息模式，它包含一个生产者、一个消费者和一个队列。
 * 生产者向队列里发送消息，消费者从队列中获取消息并消费。
 * @author liangtao
 * @Date 2020/6/15
 **/
@Configuration
public class SimpleRabbitMqConfig {

    @Bean
    public Queue queue() {
        return new Queue(MQConstans.SIMPLE_ROUTER_KEY);
    }


    @Bean
    public SimpleReciver simpleReciver(){
        return new SimpleReciver();
    }

    @Bean
    public SimpleSender simpleSender(){
        return new SimpleSender();
    }

}
