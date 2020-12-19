package com.lt.group.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author 梁先生
 * @description 群聊实现处理handler
 * @Date 2020/12/19
 **/
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel组，管理所有的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 表示连接建立，首先执行该函数
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //用户上线消息推送
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("客户端: " + channel.remoteAddress() + "上线了");
        channelGroup.add(channel);
    }
    /**
     * channel处于一个活动状态
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线");
    }
    /**
     * 调用此方法后， channelGroup 中的当前 channel 的引用会自动去除，所以不需要在调用 channelGroup 的remove方法
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("客户端： " + ctx.channel().remoteAddress() + "下线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.stream().filter(ch->!ch.equals(channel)).forEach(ch->{
            ch.writeAndFlush("客户"+ch.remoteAddress()+": "+msg+"\n");
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
