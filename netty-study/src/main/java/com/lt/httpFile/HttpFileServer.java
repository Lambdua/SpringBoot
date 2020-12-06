package com.lt.httpFile;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author 梁先生
 * @description http文件服务器
 * @Date 2020/12/3
 **/
public class HttpFileServer {
    public static void main(String[] args) throws Exception {
        int port = 8085;
        HttpFileServer entity = new HttpFileServer();
        entity.run(port);
    }

    private static final String DEFAULT_URL = "/netty-study/src/main/java/com/lt";

    public void run(final int port) throws Exception {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpFileInitialHandler());

            ChannelFuture future = sb.bind("127.0.0.1", port).sync();
            System.out.println("HTTP 文件目录服务器启动，网址是： http://127.0.0.1:" + port + DEFAULT_URL);
            future.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class HttpFileInitialHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            //添加http请求消息解码器
            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
            /**
             * 此解码器将多个消息转换为单一的 {@link io.netty.handler.codec.http.FullHttpRequest}
             * 或者{@link io.netty.handler.codec.http.FullHttpResponse}
             * 原因是Http解码器在每个HTTP消息中会生成多个消息对象:
             * 1. {@link io.netty.handler.codec.http.HttpRequest/HttpResponse}
             * 2. {@link io.netty.handler.codec.http.HttpContent}
             * 3. {@link io.netty.handler.codec.http.LastHttpContent}
             */
            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
            //新增响应Http编码器
            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
            //此handler支持异步发送大的码流（大文件的传输）,但是不占用过多的内存，防止Java内存溢出
            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
            //自己实现的 用于文件服务器的业务逻辑处理
            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(DEFAULT_URL));
        }
    }
}
