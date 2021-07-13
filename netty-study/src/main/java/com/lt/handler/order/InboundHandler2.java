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
public class InboundHandler2 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("inbound2 激活了");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf data = (ByteBuf) msg;
        System.out.println("InboundHandler2 channelRead 服务端收到数据：" + data.toString(CharsetUtil.UTF_8));
        //调用pipeline().writeAndFlush 或者 channel().writeAndFlush，还需要顺着经过Outbound了，再返回给客户端
        ctx.pipeline().writeAndFlush(Unpooled.copiedBuffer("InboundHandler2 "+data.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8));
//        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("InboundHandler2 "+data.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8));
        //调用ChannelHandlerContext.writeAndFlush，若InboundHandler2不是最后一个addLast，则OutboundHandler1、OutboundHandler1不会只想
//        ctx.writeAndFlush(Unpooled.copiedBuffer("InboundHandler2 "+data.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8));
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

