package com.lt.mq.fanout;

import com.lt.mq.Sender;
import com.lt.mq.common.MQConstans;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class FanoutSender implements Sender {
    @Autowired
    AmqpTemplate amqpTemplate;

    @Override
    public void send(String str) {
        String sendStr = "****" + str + "***" + System.currentTimeMillis();
        //设置router_key为null,使用广播模式
        amqpTemplate.convertAndSend(MQConstans.FANOUT_EXCHANGE_NAME, "", sendStr);
    }
}
