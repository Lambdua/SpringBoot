package com.lt.mq.direct;

import cn.hutool.core.util.IdUtil;
import com.lt.mq.RabbitMqServiceTestCase;
import com.lt.mq.Sender;
import com.lt.mq.Vo.ResultVo;
import com.lt.mq.common.MQConstans;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class DirectSender implements Sender {
    @Autowired
    RabbitMqServiceTestCase rabbitMqServiceTestCase;

    @Override
    public void send(Object object) {
        int i = new Random().nextInt(3);
        String sendStr = "****" + ((ResultVo) object).getMsg() + "*** router-key: " + MQConstans.DIRECT_ROUTER_KEYS.get(i);
        //设置router_key,指定到exchange绑定的指定routerkey的queue中
        rabbitMqServiceTestCase.baseSend(MQConstans.DIRECT_EXCHANGE_NAME, MQConstans.DIRECT_ROUTER_KEYS.get(i), sendStr
                , IdUtil.simpleUUID(), 10000000L);
    }
}
