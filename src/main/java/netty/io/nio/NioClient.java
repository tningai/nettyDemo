package netty.io.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * nio的客户端开发
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        //客户端新建socketChannel
        SocketChannel socketChannel = SocketChannel.open();

        //socketChannel设置为非阻塞
        socketChannel.configureBlocking(false);

        //连接指定地址的serverSocketChannel
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",8084);

        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            //如果未连接成功，则循环尝试连接
            while (!socketChannel.finishConnect()){
                System.out.println("客户端未连接到，非阻塞，可做其他工作");
            }
        }

        //向服务端发送数据
        String str = "hello,服务端";
        socketChannel.write(ByteBuffer.wrap(str.getBytes()));

        //代码停顿
        System.in.read();
    }
}
