package com.lt.mq.work;

import com.lt.mq.Receiver;
import com.lt.mq.common.MQConstans;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
@RabbitListener(queues = MQConstans.WORK_ROUTER_KEY)
public class WorkReceiver implements Receiver {
    int sort;

    public WorkReceiver(int sort) {
        this.sort = sort;
    }

    @Override
    @RabbitHandler
    public void accept(String msg) {
        System.out.print("sort:"+sort);
        System.out.println(" msg"+msg);
    }
}
