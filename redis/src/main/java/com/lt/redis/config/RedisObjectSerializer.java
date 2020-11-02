//package com.lt.redis.config;
//
//import cn.hutool.core.util.ArrayUtil;
//import org.springframework.core.serializer.support.DeserializingConverter;
//import org.springframework.core.serializer.support.SerializingConverter;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.SerializationException;
//
///**
// * @author liangtao
// * @Date 2020/7/6
// **/
//public class RedisObjectSerializer implements RedisSerializer<Object> {
//    // 做一个空数组，不是null
//    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
//    SerializingConverter serializingConverter = new SerializingConverter();
//    DeserializingConverter deserializingConverter = new DeserializingConverter();
//
//    @Override
//    public byte[] serialize(Object o) throws SerializationException {
//        if (o == null) {
//            return EMPTY_BYTE_ARRAY;
//        }
//        return serializingConverter.convert(o);
//    }
//
//    @Override
//    public Object deserialize(byte[] bytes) throws SerializationException {
//        if (ArrayUtil.isEmpty(bytes)) {
//            return null;
//        }
//        return deserializingConverter.convert(bytes);
//    }
//
//}
