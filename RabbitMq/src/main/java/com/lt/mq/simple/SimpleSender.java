package com.lt.mq.simple;

import cn.hutool.core.util.IdUtil;
import com.lt.mq.RabbitMqServiceTestCase;
import com.lt.mq.Sender;
import com.lt.mq.common.MQConstans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liangtao
 * @Date 2020/6/15
 **/
@Component
public class SimpleSender implements Sender {
    @Autowired
    RabbitMqServiceTestCase rabbitMqService;

    @Override
    public void send(Object object) {
        rabbitMqService.baseSend("",MQConstans.SIMPLE_ROUTER_KEY,
                object, IdUtil.simpleUUID(),100000L);

    }
}
