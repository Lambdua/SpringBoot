package com.lt.mq.simple;

import com.lt.mq.RabbitMqServiceTestCase;
import com.lt.mq.common.MQConstans;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author liangtao
 * @Date 2020/6/15
 * 这里通过 RabbitListener注解来指定队列
 **/
@Component
@RabbitListener(queues = MQConstans.SIMPLE_ROUTER_KEY)
public class SimpleReceiver {

    @RabbitHandler
    private void accept(Message message, Channel channel) throws IOException {
        RabbitMqServiceTestCase.baseReceiver(message, channel);
    }

}
