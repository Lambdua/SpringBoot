package com.lt.mq;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public interface Receiver {

//    @RabbitHandler
    void accept(String msg);
}
