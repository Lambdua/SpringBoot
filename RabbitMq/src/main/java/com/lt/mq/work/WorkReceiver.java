package com.lt.mq.work;

import com.lt.mq.RabbitMqServiceTestCase;
import com.lt.mq.common.MQConstans;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
@RabbitListener(queues = MQConstans.WORK_ROUTER_KEY)
public class WorkReceiver{
    int sort;

    public WorkReceiver(int sort) {
        this.sort = sort;
    }

    @RabbitHandler
    public void accept(Message message, Channel channel) throws IOException {
        System.out.print("sort:"+sort);
        RabbitMqServiceTestCase.baseReceiver(message, channel);
    }
}
