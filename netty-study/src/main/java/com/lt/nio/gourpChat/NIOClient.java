package com.lt.nio.gourpChat;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author 梁先生
 * @description 群聊系统客户端
 * @Date 2020/12/15
 **/
public class NIOClient {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(6666));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
//        socketChannel.connect(new InetSocketAddress(6666));
        String userName = socketChannel.socket().getInetAddress().getHostAddress();
        System.out.println("客户端准备完成...");

        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                for (; ; ) {
                    if (selector.select() > 0) {
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            if (!selectionKey.isReadable()) {
                                continue;
                            }
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int read = socketChannel.read(byteBuffer);
                            if (read > 0) {
                                System.out.println("接收到的消息： " + new String(byteBuffer.array(), 0, read).trim());
                            }
                            iterator.remove();
                        }
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            socketChannel.write(ByteBuffer.wrap((userName + "说：" + nextLine).getBytes()));
        }
    }

}
