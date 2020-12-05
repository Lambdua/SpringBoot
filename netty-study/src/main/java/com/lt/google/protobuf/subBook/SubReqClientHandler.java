package com.lt.google.protobuf.subBook;

import com.lt.google.protobuf.proto.SubscribeReqProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 梁先生
 * @description
 * @Date 2020/12/3
 **/
public class SubReqClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReqProto.SubscribeReq subReq(int i) {
        SubscribeReqProto.SubscribeReq.Builder reqBuilder = SubscribeReqProto.SubscribeReq.newBuilder();
        reqBuilder.setSubReqID(i).setUserName("LiangTao").setProductName("netty book")
                .setAddress("HangZhou XiHu");
        return reqBuilder.build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("从服务端接收到的响应 消息为： "+msg);
    }
}

