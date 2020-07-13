package com.lt.springLearn.mq.config;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.work.WorkReceiver;
import com.lt.springLearn.mq.work.WorkSender;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 工作模式是指向多个互相竞争的消费者发送消息的模式，它包含一个生产者、两个消费者和一个队列。
 * 两个消费者同时绑定到一个队列上去，当消费者获取消息处理耗时任务时，
 * 空闲的消费者从队列中获取并消费消息。
 * @author liangtao
 * @Date 2020/6/23
 **/
@Configuration
public class WorkRabbitConfig {
    @Bean
    public Queue workQueue() {
        return new Queue(MQConstans.WORK_ROUTER_KEY);
    }

    @Bean
    public WorkReceiver workReceiver1() {
        return new WorkReceiver(1);
    }

    @Bean
    public WorkReceiver workReceiver2() {
        return new WorkReceiver(2);
    }

    @Bean
    public WorkSender workSender() {
        return new WorkSender();
    }
}
