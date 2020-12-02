package com.lt.echoDemo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author 梁先生
 * @description 客户端处理类
 * @Date 2020/12/2
 **/
public class EchoClientHandler extends ChannelHandlerAdapter {
    private int count;
    static final String ECHO_REQ="你好，梁涛，欢迎来到netty的世界$_";


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("这是客户端第"+count++ +"次， 接收到的服务端消息为："+msg);
    }
}
