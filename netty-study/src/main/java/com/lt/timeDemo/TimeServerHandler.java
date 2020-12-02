package com.lt.timeDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.time.LocalDateTime;

/**
 * @author liangtao
 * @description
 * @date 2020年12月01 14:02
 **/
public class TimeServerHandler extends ChannelHandlerAdapter {
    private static int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //通过编码器，直接返回的就是String
        String body= (String) msg;
        System.out.println("time server receiver " + body + " count: " + count++);
        String currentTime = "QUERY DATE TIME".equals(body) ? LocalDateTime.now().toString() : "BAD REQUEST PARAM";
        currentTime+=System.getProperty("line.separator");
        ByteBuf repBuf = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(repBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("cause = " + cause);
        ctx.close();
    }
}
