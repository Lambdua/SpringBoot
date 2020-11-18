package com.lt.mq.simple;

import com.lt.mq.Receiver;
import com.lt.mq.common.MQConstans;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author liangtao
 * @Date 2020/6/15
 * 这里通过 RabbitListener注解来指定队列
 **/
@RabbitListener(queues = MQConstans.SIMPLE_ROUTER_KEY)
@Component
public class SimpleReceiver implements Receiver {
    @Override
    @RabbitHandler
    public void accept(String msg){
        System.out.println("简单模式 Receiver = " + msg);
    }

}
