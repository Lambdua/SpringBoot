package com.lt.springLearn.mq.work;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.Sender;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class WorkSender implements Sender {
    @Autowired
    AmqpTemplate amqpTemplate;

    @Override
    public void send(String str) {
        String sendStr = "****" + str + "***" + System.currentTimeMillis();
        amqpTemplate.convertAndSend(MQConstans.WORK_ROUTER_KEY, sendStr);
    }
}
