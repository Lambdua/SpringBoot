package com.lt.google.protobuf.subBook;

import com.lt.google.protobuf.proto.SubscribeRespProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author 梁先生
 * @description 图书订阅客户端
 * @Date 2020/12/3
 **/
public class SubReqClient {

    public static void main(String[] args) throws Exception {
        int port=8084;
        String host="127.0.0.1";
        SubReqClient entity=new SubReqClient();
        entity.connect(port,host);
    }
    public  void  connect(int port,String host) throws Exception{
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            Bootstrap bs=new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加ProtobufVarint32FrameDecoder,用于半包处理
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast(
                                    //添加 ProtobufDecoder,需要一个MessageLite类型,这里实际上就是告诉ProtobufDecoder
                                    //需要解码的目标类是什么，否则仅仅从字节数组中是无法判断出要解码的目标类型信息的
                                    new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance())
                            );
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new SubReqClientHandler());
                        }
                    });
            ChannelFuture future = bs.connect(host, port).sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }

    }
}
