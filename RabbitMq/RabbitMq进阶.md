# RabbitMq进阶

## 消息的可靠性投递

实现RabbitMQ消息的可靠要保证以下3点：

- **RabbitMQ消息确认机制：** RabbitMQ消息确认有2种：消息发送确认，消费接收确认。消息发送确认是确认生产者将消息发送到Exchange，Exchange分发消息至Queue的过程中，消息是否可靠投递。第一步是否到达Exchange，第二步确认是否到达Queue。
- **交换机，队列，消息进行持久化：** 防止消息发送到了broker，还没等到消费者消费 ，broker就挂掉了
- **消费者确认机制**:  模式有3种：**none(没有任何的应答会被发送)**,**auto(自动应答)**,**manual(手动应答)**。为了保证消息可靠性，我们设置手动应答，这是为什么呢？采用自动应答的方式，每次消费端收到消息后，不管是否处理完成，Broker都会把这条消息置为完成，然后从Queue中删除。如果消费端消费时，抛出异常，消费端没有成功消费该消息，从而造成消息丢失。手动应答方式可以调用**basicAck、basicNack、basicReject**方法，只有在消息得到正确处理下，再发送ACK。

#### RabbitMQ消息确认机制

##### 修改配置

```
spring:
  rabbitmq:
    host: 127.0.0.1
    username: admin123
    password: 123456
    # 该配置已过时，使用下面的配置
   	#publisher-confirms: true
    # 确认消息发送成功，通过实现ConfirmCallBack接口，消息发送到交换器Exchange后触发回调
    publisher-confirm-type: correlated
    # 实现ReturnCallback接口，如果消息从交换器发送到对应队列失败时触发
    publisher-returns: true
    listener:
    # 消息消费确认，可以手动确认
      simple:
        acknowledge-mode: manual
复制代码
```

##### 修改RabbitMqService

增加实现ConfirmCallBack接口和实现ReturnCallback接口代码

```
 // 消息发送到交换器Exchange后触发回调
    private final RabbitTemplate.ConfirmCallback confirmCallback =
            new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    if (ack) {
                        //成功业务逻辑
                        log.info("消息投递到及交换机成功啦！！！");
                    } else {
                        //失败业务逻辑
                        log.info("消息投递到及交换机失败啦！！");
                    }
                }
            };

    // 如果消息从交换器发送到对应队列失败时触发
    private final RabbitTemplate.ReturnCallback returnCallback =
            new RabbitTemplate.ReturnCallback() {
                @Override
                public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                    //失败业务逻辑
                    log.info("message=" + message.toString());
                    log.info("replyCode=" + replyCode);
                    log.info("replyText=" + replyText);
                    log.info("exchange=" + exchange);
                    log.info("routingKey=" + routingKey);
                }
            };
复制代码
```

在rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor, correlationData)之前增加如下代码：

```
    rabbitTemplate.setConfirmCallback(this.confirmCallback);
        rabbitTemplate.setReturnCallback(this.returnCallback);
复制代码
```

##### 测试消息到交换机是否成功

为了方便测试，用controller发送消息。消息路由不到合适的Exchange，Confirm机制回送的ACK会返回false，走异常处理，进行一些业务逻辑，如重试或者补偿等手段

#### 交换机，队列，消息进行持久化

这个在前文中已提到，略。

#### 消费者确认机制

前面提到有这3种手动应答方式basicAck、basicNack、basicReject，那么先了解一下。

##### basicAck

当multiple为false，只确认当前的消息。当multiple为true，批量确认所有比当前deliveryTag小的消息。deliveryTag是用来标识Channel中投递的消息。RabbitMQ保证在每个Channel中，消息的deliveryTag是从1递增。

```
   public void basicAck(long deliveryTag, boolean multiple) throws IOException {
        this.transmit(new Ack(deliveryTag, multiple));
        this.metricsCollector.basicAck(this, deliveryTag, multiple);
    }
复制代码
```

##### basicNack

当消费者消费消息时出现异常了，那么可以使用这种方式。当requeue为true，失败消息会重新进入Queue，一般结合重试机制使用，当重试次数超过最大值，丢弃该消息)或者是死信队列+重试队列。当requeue为false，丢弃该消息。

```
public void basicNack(long deliveryTag, boolean multiple, boolean requeue) throws IOException {
        this.transmit(new Nack(deliveryTag, multiple, requeue));
        this.metricsCollector.basicNack(this, deliveryTag);
    }
复制代码
```

##### basicReject

和basicNack用法一样。

##### 测试

先把手动确定注释掉

```
 @RabbitListener(queues = TEST_QUEUE)
    public void t2(Message message, Channel channel) throws IOException {
            String msg = new String(message.getBody());
            System.out.println("消费者1收到=-=" + msg);
//            long deliveryTag = message.getMessageProperties().getDeliveryTag();
//            channel.basicAck(deliveryTag,false);
    }
复制代码
```

###### 查看管理界面

消息变成unacked ![img](.\img\mq管理图1)

停止消费者程序，消息又变成ready，这是因为虽然我们设置了手动ACK，但是代码中并没有进行消息确认！所以消息并未被真正消费掉。当我们关掉这个消费者，消息的状态再次称为Ready

![img](.\img\mq管理图2)

