package com.lt.mq.direct;

import com.lt.mq.RabbitMqServiceTestCase;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class DirectReceiver  {

    //表达式: == fanoutQueue1().getName;
    @RabbitListener(queues = "#{directQueue1.name}")
    public void reveive1(Message message, Channel channel) throws IOException {
        System.out.println("fanoutType:1 ");
        accept(message,channel);
    }

    @RabbitListener(queues = "#{directQueue2.name}")
    public void reveive2(Message message, Channel channel) throws IOException {
        System.out.println("fanoutType:2 ");
        accept(message,channel);
    }
    private void accept(Message message, Channel channel) throws IOException {
        RabbitMqServiceTestCase.baseReceiver(message, channel);
    }
}
