package com.lt.google.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lt.google.protobuf.proto.SubscribeReqProto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 梁先生
 * @description proto生成测试
 * @Date 2020/12/3
 **/
public class TestSubscribeReqProto {
    //将SubscribeReq序列化
    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    //将SubscribeReq反序列化
    private static SubscribeReqProto.SubscribeReq decode(byte[] body)
            throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    //创建SubscribeReq对象
    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.
                SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("test");
        builder.setProductName("Netty book");
        List<String> address = new ArrayList<>();
        address.add("NanJing XuanWuHu");
        address.add("BeiJing TianAnMen");
        address.add("HangZhou XiHu");
        builder.setAddress("HangZhou XiHu");
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode : " + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("After decode : " + req.toString());
        System.out.println("Assert equal : -->" + req2.equals(req));
    }

}
