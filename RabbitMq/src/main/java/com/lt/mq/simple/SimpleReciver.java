package com.lt.mq.simple;

import com.lt.mq.Receiver;
import com.lt.mq.common.MQConstans;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author liangtao
 * @Date 2020/6/15
 **/
@RabbitListener(queues = MQConstans.SIMPLE_ROUTER_KEY)
public class SimpleReciver implements Receiver {
    @Override
    @RabbitHandler
    public void accept(String msg){
        System.out.println("Reciver = " + msg);
    }

}
