package com.lt.springLearn.mq.config;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.topic.TopicReceiver;
import com.lt.springLearn.mq.topic.TopicSender;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liangtao
 * @Date 2020/6/23
 * 添加通配符模式相关Java配置，创建一个名为exchange.topic的交换机
 * 一个生产者、两个消费者和两个匿名队列
 * 匹配*.orange.*和*.*.rabbit发送到队列1
 * 匹配lazy.#发送到队列2
 **/
@Configuration
public class TopicRabbitConfig {


    @Bean
    public Binding topicBinding1a(TopicExchange topicExchange, Queue topicQueue1) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with(MQConstans.TOPIC_BINDING_KEYS.get(0));
    }

    @Bean
    public Binding topicBinding1b(TopicExchange topicExchange, Queue topicQueue1) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with(MQConstans.TOPIC_BINDING_KEYS.get(1));
    }

    @Bean
    public Binding topicBinding2(TopicExchange topicExchange, Queue topicQueue2) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with(MQConstans.TOPIC_BINDING_KEYS.get(2));
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(MQConstans.TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue topicQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue topicQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public TopicSender topicSender() {
        return new TopicSender();
    }

    @Bean
    public TopicReceiver topicReceiver(){
        return new TopicReceiver();
    }


}
