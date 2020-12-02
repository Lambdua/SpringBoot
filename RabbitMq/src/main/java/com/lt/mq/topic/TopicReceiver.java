package com.lt.mq.topic;

import com.lt.mq.RabbitMqServiceTestCase;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class TopicReceiver  {

    @RabbitListener(queues = "#{topicQueue1.name}")
    public void receive1(Message message, Channel channel) throws IOException {
        System.out.println("topicReceive1: ");
        accept( message,channel);
    }

    @RabbitListener(queues = "#{topicQueue2.name}")
    public void receive2(Message message, Channel channel) throws IOException {
        System.out.println("topicReceive2");
        accept(message,channel);
    }

    private void accept(Message message, Channel channel) throws IOException {
        RabbitMqServiceTestCase.baseReceiver(message, channel);
    }


}
