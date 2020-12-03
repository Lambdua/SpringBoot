package com.lt.google.protobuf.subBook;

import com.lt.google.protobuf.proto.SubscribeReqProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author 梁先生
 * @description 订阅服务端
 * @Date 2020/12/3
 **/
public class SubReqServer {

    public static void main(String[] args) throws Exception {
        int port=8084;
        SubReqServer entity=new SubReqServer();
        entity.bind(port);
    }


    public void bind(int port) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /**
                             *  protobufDecoder仅仅负责解码，并不支持读半包，因此，在protobufDecoder前面
                             *  添加ProtobufVarint32FrameDecoder,用于半包处理。
                             *  一般有三种方式可以选择
                             *  1. 使用Netty提供的ProtobufVarint32FrameDecoder
                             *  2. 继承Netty提供的通用半包解码器 LengthFieldBasedFrameDecoder;
                             *  3. 继承ByteToMesageDecoder类，自己处理半包消息
                             */
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast(
                                    //添加 ProtobufDecoder,需要一个MessageLite类型,这里实际上就是告诉ProtobufDecoder
                                    //需要解码的目标类是什么，否则仅仅从字节数组中是无法判断出要解码的目标类型信息的
                                    new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance())
                            );
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new SubReqServerHandler());
                        }
                    });

            ChannelFuture future = sb.bind(port).sync();

            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
