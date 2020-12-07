package com.lt.http.json.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author 梁先生
 * @description http json格式解码抽象类
 * @date 2020/12/7
 **/
public abstract class AbstractHttpJsonDecoder<T> extends MessageToMessageDecoder<T> {
    private ObjectMapper om = new ObjectMapper();
    private Class<?> clazz;

    public AbstractHttpJsonDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    final static String UTF_8_Str = StandardCharsets.UTF_8.name();
    final static Charset UTF_8 = StandardCharsets.UTF_8;


    protected Object decode0(ChannelHandlerContext arg0, ByteBuf body) {
        try {
            Object result = om.readValue(body.toString(UTF_8), clazz);
            System.out.println("body: " + body);
            return result;
        } catch (JsonProcessingException e) {
            System.out.println("不支持的格式转换： " + body + " --->转换类型:" + clazz);
            System.out.println(e);
            return null;
        }
    }
}
