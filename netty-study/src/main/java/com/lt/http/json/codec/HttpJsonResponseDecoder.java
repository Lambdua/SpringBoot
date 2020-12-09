package com.lt.http.json.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * @author 梁先生
 * @description
 * @Date 2020/12/8
 **/
public class HttpJsonResponseDecoder extends AbstractHttpJsonDecoder<DefaultFullHttpResponse> {
    public HttpJsonResponseDecoder(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
        out.add(new HttpJsonResponse(msg, decode0(ctx, msg.content())));
    }
}
