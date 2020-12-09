package com.lt.http.json.server;

import com.lt.http.json.codec.HttpJsonRequestDecoder;
import com.lt.http.json.codec.HttpJsonResponseEncoder;
import com.lt.http.json.pojo.Order;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;

/**
 * @author 梁先生
 * @description server
 * @Date 2020/12/8
 **/
public class HttpJsonServer {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new HttpJsonServer().run(port);
    }

    public void run(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)

                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
//                            1、ctx.writeAndFlush只会从当前的handler位置开始，往前找outbound执行
//                            2、ctx.pipeline().writeAndFlush与ctx.channel().writeAndFlush会从tail的位置开始，往前找outbound执行
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("json-decoder", new HttpJsonRequestDecoder(Order.class));
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("json-encoder", new HttpJsonResponseEncoder());
                            ch.pipeline().addLast("jsonServerHandler", new HttpJsonServerHandler());
                        }
                    });
            System.out.println("HTTP订购服务器启动，网址是 : " + "http://localhost:" + port);
            sb.bind(new InetSocketAddress(port)).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
