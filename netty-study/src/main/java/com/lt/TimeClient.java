package com.lt;

import cn.hutool.core.util.ArrayUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author liangtao
 * @description
 * @date 2020年12月01 14:35
 **/
public class TimeClient {

    public static void main(String[] args) throws InterruptedException {
        int port = 8082;
        if (ArrayUtil.isNotEmpty(args)) {
            port = Integer.valueOf(args[0]);
        }
        new TimeClient().connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) throws InterruptedException {
        //配置客户端的NIO线程组
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            ChannelFuture channelFuture = b.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    }).connect(host, port);
            channelFuture.sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }
}
