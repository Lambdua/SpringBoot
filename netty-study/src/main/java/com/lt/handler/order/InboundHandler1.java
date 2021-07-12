package com.lt.handler.order;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author 梁先生
 * @description
 * @Date 2020/12/20
 **/
public class InboundHandler1 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("inbound1 激活了");
        ctx.write("in1");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf data = (ByteBuf) msg;
        System.out.println("InboundHandler1 channelRead 服务端收到数据：" + data.toString(CharsetUtil.UTF_8));
        // 执行下一个InboundHandler
        ctx.fireChannelRead(Unpooled.copiedBuffer("InboundHandler1 "+data.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}

