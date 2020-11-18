package com.lt.mq.config;

import org.springframework.context.annotation.Configuration;

/**
 * 简单模式是最简单的消息模式，它包含一个生产者、一个消费者和一个队列。
 * 生产者向队列里发送消息，消费者从队列中获取消息并消费。
 * @author liangtao
 * @Date 2020/6/15
 **/
@Configuration
public class SimpleRabbitMqConfig {

    /**
     * 这里队列都不用设置，直接使用默认队列
     */
//    @Bean
//    public Queue queue() {
//        return new Queue(MQConstans.SIMPLE_ROUTER_KEY);
//    }



}
