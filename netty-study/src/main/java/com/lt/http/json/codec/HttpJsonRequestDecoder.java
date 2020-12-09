package com.lt.http.json.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author 梁先生
 * @description http json 请求的解码器
 * @Date 2020/12/7
 **/
public class HttpJsonRequestDecoder extends AbstractHttpJsonDecoder<FullHttpRequest> {

    public HttpJsonRequestDecoder(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) {
        System.out.println("--------in--------jsonRequestDecoder");
        if (!msg.decoderResult().isSuccess()) {
            //如果失败，返回400
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        //生成一个HttpJsonRequest加入到out中
        out.add(new HttpJsonRequest(msg, decode0(ctx, msg.content())));

    }

    private static void sendError(ChannelHandlerContext ctx,
                                  HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                status, Unpooled.copiedBuffer("Failure: " + status.toString()
                + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
