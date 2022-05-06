package netty.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * nio的服务端开发
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        //创建serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(8084));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //创建selector注册器
        Selector selector = Selector.open();

        //将serverSocketChannel注册进selector，监听accept客户端申请连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //开始循环监听
        while (true) {
            //设置监听方法
            if (selector.select(1000) == 0) {
                //select方法监听1s没有事件发生就返回
                System.out.println("server等待1秒返回");
                continue;
            }

            //selector注册器轮询发生的事件，生成selectKey的set集合
            //集合selectKeys中包含了目前通道中的发生的事务
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历set集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();

                //判断此时的事件是否为accept
                if (selectionKey.isAcceptable()) {
                    //已连接成功，则可以新建和客户端通信的socketChannel通道
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    //新连接的socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);

                    System.out.println("服务端连接客户端：" + socketChannel.hashCode());

                    //将已连接的socketChannel注册进selector中，并引入服务端的缓冲区
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                //判断此时的事件是否为read
                if (selectionKey.isReadable()) {
                    //根据selectKey拿到socketChannel通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //将socketChannel对应缓冲区中数据读出
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println("读取数据：" + new String(byteBuffer.array()));
                }

                //手动从集合中删除selectKey
                iterator.remove();
            }
        }

    }
}
