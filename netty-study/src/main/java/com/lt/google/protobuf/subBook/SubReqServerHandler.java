package com.lt.google.protobuf.subBook;

import com.lt.google.protobuf.proto.SubscribeReqProto;
import com.lt.google.protobuf.proto.SubscribeRespProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author 梁先生
 * @description 订阅处理类
 * @Date 2020/12/3
 **/
@ChannelHandler.Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ProtobufDecoder已经对消息进行了自动解码，所以这里可以直接转换使用。
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        if ("LiangTao".equalsIgnoreCase(req.getUserName())) {
            System.out.println("服务端接收到的消息是: " + req.toString());
            //因为使用了ProtobufEncoder进行了编码，因为不用在手动编码，直接返回即可;
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    protected SubscribeRespProto.SubscribeResp resp(int subReqID) {
        SubscribeRespProto.SubscribeResp.Builder respBuilder = SubscribeRespProto.SubscribeResp.newBuilder();
        //这里的中文响应消息接收到会乱码。以后在处理
        respBuilder.setSubReqID(subReqID).setRespCode("0").setDesc("netty book 订单成功，三天后按照地址发货");
        return respBuilder.build();
    }
}
