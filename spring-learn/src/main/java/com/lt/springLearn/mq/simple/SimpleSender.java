package com.lt.springLearn.mq.simple;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.Sender;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author liangtao
 * @Date 2020/6/15
 **/
public class SimpleSender implements Sender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void send(String i){
        String sender="**"+i+"**hello mq "+ LocalDateTime.now();
        amqpTemplate.convertAndSend(MQConstans.SIMPLE_ROUTER_KEY,sender);
    }
}
