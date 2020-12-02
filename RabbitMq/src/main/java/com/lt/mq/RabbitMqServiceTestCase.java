package com.lt.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lt.mq.common.MQConstans;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * @author 梁先生
 * @description 相关配置模板用例
 * @Date 2020/11/21
 **/
@Service
public class RabbitMqServiceTestCase {

    private static final ObjectMapper om=new ObjectMapper();
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*发送消息到队列*/
    public String sendQueue(Object payload) {
        return baseSend("", MQConstans.TEST_QUEUE, payload, null, null);
    }


    public static void baseReceiver(Message message, Channel channel) throws IOException {
        String messStr = new String(message.getBody());
//        ResultVo resultVo = om.readValue(messStr, ResultVo.class);
        System.out.println("resultVo = " + messStr);
        //手动确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * MQ 公用发送方法
     *
     * @param exchange              交换机
     * @param routingKey            队列
     * @param payload               消息体
     * @param messageId             消息id（唯一性）
     * @param messageExpirationTime 持久化时间
     * @return 消息编号
     */
    public String baseSend(String exchange, String routingKey, Object payload, String messageId, Long messageExpirationTime) {
        /*若为空，则自动生成*/
        if (messageId == null) {
            messageId = UUID.randomUUID().toString();
        }
        String finalMessageId = messageId;
        /*设置消息属性*/
        MessagePostProcessor messagePostProcessor = (message) -> {
            /*消息属性中写入消息id*/
            message.getMessageProperties().setMessageId(finalMessageId);
            /*设置消息持久化时间*/
            if (!StringUtils.isEmpty(messageExpirationTime)) {
                message.getMessageProperties().setExpiration(messageExpirationTime.toString());
            }
            /*设置消息持久化*/
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        };

        /*构造消息体，转换json数据格式*/
        Message message = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(payload);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentEncoding(MessageProperties.CONTENT_TYPE_JSON);
            message = new Message(json.getBytes(), messageProperties);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }

        /*表示当前消息唯一性*/
        CorrelationData correlationData = new CorrelationData(finalMessageId);

        /**
         * public void convertAndSend(String exchange, String routingKey, Object message,
         * MessagePostProcessor messagePostProcessor, @Nullable CorrelationData correlationData) throws AmqpException
         * exchange: 路由
         * routingKey: 绑定key
         * message: 消息体
         * messagePostProcessor: 消息属性处理器
         * correlationData: 表示当前消息唯一性
         */
        rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor, correlationData);
        return finalMessageId;
    }
}
