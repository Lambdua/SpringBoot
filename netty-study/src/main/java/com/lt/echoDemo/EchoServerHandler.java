package com.lt.echoDemo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 梁先生
 * @description server消息处理类
 * @Date 2020/12/2
 **/
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("这是第" + count++ + "次，接受到的消息： " + body);
        body += "$_";
        ctx.writeAndFlush(Unpooled.copiedBuffer(body.getBytes()));
    }
}
