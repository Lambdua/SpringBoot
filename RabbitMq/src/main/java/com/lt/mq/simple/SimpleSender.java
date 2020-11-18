package com.lt.mq.simple;

import com.lt.mq.Sender;
import com.lt.mq.common.MQConstans;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author liangtao
 * @Date 2020/6/15
 **/
@Component
public class SimpleSender implements Sender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void send(String i){
        String sender="**"+i+"**hello mq "+ LocalDateTime.now();
        //指定router_Key,这里的routerKey直接会定位到一个队列中
        amqpTemplate.convertAndSend(MQConstans.SIMPLE_ROUTER_KEY,sender);
    }
}
