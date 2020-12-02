package com.lt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author liangtao
 * @description
 * @date 2020年12月01 14:42
 **/
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    byte[] req;
    private int couont=0;
    public TimeClientHandler() {
        this.req=("QUERY DATE TIME"+System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message=null;
        for (int i = 0; i < 100; i++) {
            message=Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf= (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String reqStr = new String(req, StandardCharsets.UTF_8);
        System.out.println("现在时间是 = " + reqStr+" count : "+couont++);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
        ctx.close();
    }
}
