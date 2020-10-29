package com.lt.mq.topic;

import com.lt.mq.Receiver;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class TopicReceiver implements Receiver {

    @RabbitListener(queues = "#{topicQueue1.name}")
    public void receive1(String msg){
        accept("topicReceive1: "+msg);
    }

    @RabbitListener(queues = "#{topicQueue2.name}")
    public void receive2(String msg){
        accept("topicReceive2: "+msg);
    }


    @Override
    public void accept(String msg) {
        System.out.println(msg);
    }
}
