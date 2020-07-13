package com.lt.springLearn.mq.direct;

import com.lt.springLearn.common.MQConstans;
import com.lt.springLearn.mq.Sender;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class DirectSender implements Sender {
    @Autowired
    AmqpTemplate amqpTemplate;
    @Override
    public void send(String str) {
        int i = new Random().nextInt(3);
        String sendStr = "****" + str + "*** router-key: "+ MQConstans.DIRECT_ROUTER_KEYS.get(i);
        //设置router_key,指定到exchange绑定的指定routerkey的queue中
        amqpTemplate.convertAndSend(MQConstans.DIRECT_EXCHANGE_NAME, MQConstans.DIRECT_ROUTER_KEYS.get(i), sendStr);

    }
}
