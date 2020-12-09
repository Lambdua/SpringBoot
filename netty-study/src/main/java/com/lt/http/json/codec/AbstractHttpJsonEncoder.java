package com.lt.http.json.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author 梁先生
 * @description http json格式解析抽象类
 * @date 2020/12/7
 **/
public abstract class AbstractHttpJsonEncoder<T> extends MessageToMessageEncoder<T> {
    private ObjectMapper om = new ObjectMapper();

    final static Charset UTF_8 = StandardCharsets.UTF_8;

    protected ByteBuf encode0(ChannelHandlerContext ctx, Object body) {
        try {
            String msgJsonStr = om.writeValueAsString(body);
            return Unpooled.copiedBuffer(msgJsonStr, UTF_8);
        } catch (JsonProcessingException e) {
            System.out.println("json转换错误" + e);
            return Unpooled.EMPTY_BUFFER;
        }
    }


}
