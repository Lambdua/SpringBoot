package com.lt.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author 梁先生
 * @description
 * @Date 2020/12/15
 **/
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        if (!socketChannel.connect(new InetSocketAddress(6666))){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，客户端可以做其他事情。。");
            }
        }
        String test="hello liangtao";
        ByteBuffer byteBuffer=ByteBuffer.wrap(test.getBytes(StandardCharsets.UTF_8));
        socketChannel.write(byteBuffer);
    }
}
