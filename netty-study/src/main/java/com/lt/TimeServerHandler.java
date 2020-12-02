package com.lt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author liangtao
 * @description
 * @date 2020年12月01 14:02
 **/
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private static int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        String body = new String(req, StandardCharsets.UTF_8).substring(0, req.length -
                System.getProperty("line.separator").length());
        System.out.println("time server receiver " + body + " count: " + count++);
        String currentTime = "QUERY DATE TIME".equals(body) ? LocalDateTime.now().toString() : "BAD REQUEST PARAM";
        ByteBuf repBuf = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(repBuf);
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
