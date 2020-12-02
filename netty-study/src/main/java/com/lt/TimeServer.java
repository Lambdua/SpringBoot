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

/**
 * @author liangtao
 * @description
 * @date 2020年12月01 13:54
 **/
public class TimeServer {
    public static void main(String[] args) {
        int port=8082;
        if (ArrayUtil.isNotEmpty(args)){
            port=Integer.valueOf(args[0]);
        }
        new TimeServer().bind(port);
    }
    public void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            ChannelFuture channelFuture = sb.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChileChannelHandler())
                    .bind(port);
            channelFuture.sync();

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    private class ChileChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }
}
