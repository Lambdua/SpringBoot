package com.lt;

import cn.hutool.core.util.ArrayUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author liangtao
 * @description
 * @date 2020年12月01 13:54
 **/
public class TimeServer {
    public static void main(String[] args) throws InterruptedException {
        int port=8082;
        if (ArrayUtil.isNotEmpty(args)){
            port=Integer.valueOf(args[0]);
        }
        new TimeServer().bind(port);
    }
    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            ChannelFuture channelFuture = sb.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChileChannelHandler())
                    .bind(port).sync();

            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    private class ChileChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            //这里解决粘包拆包问题,这里添加了两个解码器
            //第一个是 LineBasedFrameDecoder
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            //第二个是StringDecoder
            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }
}
