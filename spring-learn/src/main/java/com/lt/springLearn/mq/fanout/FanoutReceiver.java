package com.lt.springLearn.mq.fanout;

import com.lt.springLearn.mq.Receiver;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class FanoutReceiver implements Receiver {


    //表达式: == fanoutQueue1().getName;
    @RabbitListener(queues = "#{fanoutQueue1.name}")
    public void reveive1(String msg) {
        msg = "type:1 " + msg;
        accept(msg);
    }

    @RabbitListener(queues = "#{fanoutQueue2.name}")
    public void reveive2(String msg) {
        msg = "type:2 " + msg;
        accept(msg);
    }


    @Override
    public void accept(String msg) {
        System.out.println(" msg: " + msg);
    }
}
