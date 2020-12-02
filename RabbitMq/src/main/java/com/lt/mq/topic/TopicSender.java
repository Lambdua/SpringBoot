package com.lt.mq.topic;

import cn.hutool.core.util.IdUtil;
import com.lt.mq.RabbitMqServiceTestCase;
import com.lt.mq.Sender;
import com.lt.mq.common.MQConstans;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class TopicSender implements Sender , RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitMqServiceTestCase rabbitMqServiceTestCase;
    public void sendExchange(Object o , String exchange){
        //添加代码 进行消息确认
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
        for (String routerKey : MQConstans.TOPIC_ROUTER_KEYs) {
            rabbitMqServiceTestCase.baseSend(exchange,routerKey,o,
                    IdUtil.simpleUUID(),10000L);
        }
    }

    /**
     * 消息发送到交换器Exchange后出发回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            //发送成功回调后的业务逻辑
            System.out.println("消息投递到交换机成功");
        } else {
            System.out.println("消息投递到交换机失败");
        }
    }

    /**
     * 实现ReturnCallback接口
     * 如果消息从交换器发送到对应的队列失败时会触发
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("-------交换器发送到队列失败----------");
        System.out.println("message = " + message);
        System.out.println("replyCode = " + replyCode);
        System.out.println("replyText = " + replyText);
        System.out.println("exchange = " + exchange);
        System.out.println("routingKey = " + routingKey);
        System.out.println("----------------");
    }

    @Override
    public void send(Object object) {
        for (String routerKey : MQConstans.TOPIC_ROUTER_KEYs) {
            String send="router-key: "+routerKey;
            //topic模式。必须指定exchange,否则无效
            rabbitMqServiceTestCase.baseSend(MQConstans.TOPIC_EXCHANGE_NAME,routerKey,send, IdUtil.simpleUUID(),10000L);
        }
    }
}
