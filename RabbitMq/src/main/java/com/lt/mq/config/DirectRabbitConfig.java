package com.lt.mq.config;

import com.lt.mq.common.MQConstans;
import com.lt.mq.direct.DirectReceiver;
import com.lt.mq.direct.DirectSender;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liangtao
 * @Date 2020/6/23
 * 路由模式是可以根据路由键选择性给多个消费者发送消息的模式
 * 它包含一个生产者、两个消费者、两个队列和一个交换机。
 * 两个消费者同时绑定到不同的队列上去，两个队列通过路由键绑定到交换机上去
 * 生产者发送消息到交换机，交换机通过路由键转发到不同队列，队列绑定的消费者接收并消费消息。
 * 注意： 一个消息最终只会发送到多个队列上，对于同一个routerKey绑定到多个队列的，多个队列都会收到消息，类似广播
 **/
@Configuration
public class DirectRabbitConfig {

    @Bean
    public Binding directBinding1a(DirectExchange directExchange, Queue directQueue1) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with(MQConstans.DIRECT_ROUTER_KEYS.get(0));
    }

    /*
     将directQueue1绑定到exchange中,指定router-key ↑↓ 上下两个方法，为一个queue制定了两个router-key
     */

    @Bean
    public Binding directBinding1b(DirectExchange directExchange, Queue directQueue1) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with(MQConstans.DIRECT_ROUTER_KEYS.get(1));
    }


    //同上,和queue1 有一个共同的router-key:Constants.DIRECT_ROUTER_KEYS.get(1)
    @Bean
    public Binding directBinding2a(DirectExchange directExchange, Queue directQueue2) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with(MQConstans.DIRECT_ROUTER_KEYS.get(1));
    }

    @Bean
    public Binding directBinding2b(DirectExchange directExchange, Queue directQueue2) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with(MQConstans.DIRECT_ROUTER_KEYS.get(2));
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MQConstans.DIRECT_EXCHANGE_NAME);
    }

    @Bean
    public Queue directQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue directQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public DirectReceiver directReceiver() {
        return new DirectReceiver();
    }

    @Bean
    public DirectSender directSender() {
        return new DirectSender();
    }

}
