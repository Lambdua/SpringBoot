package com.lt.http.json.server;

import com.lt.http.json.codec.HttpJsonRequest;
import com.lt.http.json.codec.HttpJsonResponse;
import com.lt.http.json.pojo.Address;
import com.lt.http.json.pojo.Order;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.List;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author 梁先生
 * @description jsonHttp server handler
 * @date 2020/12/8
 **/
public class HttpJsonServerHandler extends SimpleChannelInboundHandler<HttpJsonRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpJsonRequest jsonRequest) {
        System.out.println("--------in----httpJsonServerHandler");
        FullHttpRequest request = jsonRequest.getRequest();
        Order order = (Order) jsonRequest.getBody();
        System.out.println("Http服务器接收请求: " + order);
        doSomething(order);
        ChannelFuture future = ctx.writeAndFlush(new HttpJsonResponse(null, order));
//        ChannelFuture future = ctx.channel().writeAndFlush(new HttpJsonResponse(null, order));
        if (!HttpUtil.isKeepAlive(request)) {
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future)  {
                    ctx.close();
                }
            });
        }

    }

    private void doSomething(Order order) {
        order.getCustomer().setFirstName("狄");
        order.getCustomer().setLastName("仁杰");
        List<String> midNames = new ArrayList<>();
        midNames.add("李元芳");
        order.getCustomer().setMiddleNames(midNames);
        Address address = order.getBillTo();
        address.setCity("洛阳");
        address.setCountry("大唐");
        address.setState("河南道");
        address.setPostCode("123456");
        order.setBillTo(address);
        order.setShipTo(address);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                status, Unpooled.copiedBuffer("失败: " + status.toString()
                + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
