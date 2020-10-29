package com.lt.mq.direct;

import com.lt.mq.Receiver;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class DirectReceiver implements Receiver {

    //表达式: == fanoutQueue1().getName;
    @RabbitListener(queues = "#{directQueue1.name}")
    public void reveive1(String msg) {
        msg = "fanoutType:1 " + msg;
        accept(msg);
    }

    @RabbitListener(queues = "#{directQueue1.name}")
    public void reveive2(String msg) {
        msg = "fanoutType:2 " + msg;
        accept(msg);
    }


    @Override
    public void accept(String msg) {
        System.out.println(" msg: " + msg);
    }
}
