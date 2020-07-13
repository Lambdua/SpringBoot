package com.lt.springLearn.mq.simple;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.Receiver;
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
