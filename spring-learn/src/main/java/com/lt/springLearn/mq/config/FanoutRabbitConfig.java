package com.lt.springLearn.mq.config;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.fanout.FanoutReceiver;
import com.lt.springLearn.mq.fanout.FanoutSender;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布/订阅模式是指同时向多个消费者发送消息的模式（类似广播的形式）
 * 它包含一个生产者、两个消费者、两个队列和一个交换机。
 * 两个消费者同时绑定到不同的队列上去，两个队列绑定到交换机上去
 * 生产者通过发送消息到交换机，所有消费者接收并消费消息。
 *
 * @author liangtao
 * @Date 2020/6/23
 **/
@Configuration
public class FanoutRabbitConfig {
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(MQConstans.FANOUT_EXCHANGE_NAME);
    }

    @Bean
    public Queue fanoutQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue fanoutQueue2(){
        return new Queue("q2");
    }

    @Bean
    public Binding fanoutBinding1(FanoutExchange fanoutExchange,Queue fanoutQueue1){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding fanoutBinding2(FanoutExchange fanoutExchange,Queue fanoutQueue2){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

    @Bean
    public FanoutSender fanoutSender(){
        return new FanoutSender();
    }


    @Bean
    public FanoutReceiver fanoutReceiver() {
        return new FanoutReceiver();
    }



}
