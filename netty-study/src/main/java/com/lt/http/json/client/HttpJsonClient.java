package com.lt.http.json.client;

import com.lt.http.json.codec.HttpJsonRequestEncoder;
import com.lt.http.json.codec.HttpJsonResponseDecoder;
import com.lt.http.json.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.InetSocketAddress;

/**
 * @author 梁先生
 * @description
 * @Date 2020/12/8
 **/
public class HttpJsonClient {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new HttpJsonClient().connect(port);
    }

    public void connect(int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast("http-decoder", new HttpResponseDecoder())
                                    .addLast("http-aggregator", new HttpObjectAggregator(65536))
                                    .addLast("json-decoder", new HttpJsonResponseDecoder(Order.class))
                                    .addLast("http-encoder", new HttpRequestEncoder())
                                    .addLast("json-encoder", new HttpJsonRequestEncoder())
                                    .addLast("jsonClientHandler", new HttpJsonClientHandler());
                        }
                    });

            ChannelFuture future = b.connect(new InetSocketAddress(port)).sync();

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
