package com.lt.mq.fanout;

import cn.hutool.core.util.IdUtil;
import com.lt.mq.RabbitMqServiceTestCase;
import com.lt.mq.Sender;
import com.lt.mq.common.MQConstans;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class FanoutSender implements Sender {
    @Autowired
    RabbitMqServiceTestCase rabbitMqService;


    @Override
    public void send(Object object) {
        rabbitMqService.baseSend(MQConstans.FANOUT_EXCHANGE_NAME, "",
                object, IdUtil.simpleUUID(), 100000L);

    }
}
