package com.lt.mq.work;

import cn.hutool.core.util.IdUtil;
import com.lt.mq.RabbitMqServiceTestCase;
import com.lt.mq.Sender;
import com.lt.mq.common.MQConstans;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class WorkSender implements Sender {
    @Autowired
    RabbitMqServiceTestCase rabbitMqServiceTestCase;

    @Override
    public void send(Object object) {
        rabbitMqServiceTestCase.baseSend("", MQConstans.WORK_ROUTER_KEY, object
                , IdUtil.simpleUUID(), 100000L);
    }
}
