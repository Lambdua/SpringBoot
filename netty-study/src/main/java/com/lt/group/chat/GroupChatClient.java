package com.lt.group.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author 梁先生
 * @description 群聊系统客户端
 * @Date 2020/12/19
 **/
public class GroupChatClient {
    private int port=6666;
    private String host="127.0.0.1";
    public static void main(String[] args) throws InterruptedException {
        GroupChatClient client=new GroupChatClient();
        client.run();
    }

    public void run() throws InterruptedException {
        EventLoopGroup workGroup= new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加一个Sting解码器
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            //添加一个编码器
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            //添加自己的业务处理handler
                            ch.pipeline().addLast(new GroupChatClientHandler());
                        }
                    });
            ChannelFuture future = bs.connect(host, port).sync();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                future.channel().writeAndFlush(line+"\r\n");
            }
        } finally {
            workGroup.shutdownGracefully();
        }
    }


}
