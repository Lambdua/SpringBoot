package com.lt.http.json.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;

/**
 * @author 梁先生
 * @description jsonResponse编码器
 * @Date 2020/12/8
 **/
public class HttpJsonResponseEncoder extends AbstractHttpJsonEncoder<HttpJsonResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpJsonResponse msg, List<Object> out) {
        System.out.println("---------out-----jsonResponseEncoder");
        ByteBuf body = encode0(ctx, msg.getResult());
        FullHttpResponse response = msg.getResponse();
        if (response == null) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        } else {
            response = new DefaultFullHttpResponse(response.protocolVersion()
                    , response.status(), body);
        }
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        HttpUtil.setContentLength(response, body.readableBytes());
        out.add(response);
    }
}
