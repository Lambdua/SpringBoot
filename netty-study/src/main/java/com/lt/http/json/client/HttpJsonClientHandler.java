package com.lt.http.json.client;

import com.lt.http.json.OrderFactory;
import com.lt.http.json.codec.HttpJsonRequest;
import com.lt.http.json.codec.HttpJsonResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 梁先生
 * @description
 * @Date 2020/12/8
 **/
public class HttpJsonClientHandler extends SimpleChannelInboundHandler<HttpJsonResponse> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        HttpJsonRequest request = new HttpJsonRequest(null, OrderFactory.create(123));
        ctx.writeAndFlush(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpJsonResponse msg) {
        System.out.println("客户端收到的HTTP标头响应为：" + msg.getResponse().headers().names());
        System.out.println("客户端收到的http正文响应为：" + msg.getResult());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
