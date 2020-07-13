package com.lt.springLearn.mq.topic;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.Sender;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class TopicSender implements Sender {
    @Autowired
    AmqpTemplate amqpTemplate;

    @Override
    public void send(String str) {
        //每收到一个消息轮询router-keys 发送
        for (String routerKey : MQConstans.TOPIC_ROUTER_KEYs) {
            String send="router-key: "+routerKey;
            //topic模式。必须指定exchange,否则无效
            amqpTemplate.convertAndSend(MQConstans.TOPIC_EXCHANGE_NAME,routerKey,send);
        }
    }
}
