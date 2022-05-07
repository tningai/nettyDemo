package netty.io.group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 群聊系统的server端
 * 1、监听端口
 * 2、创建selector，解析事件
 * 3、创建socketChannel完成通讯
 */
public class GroupServerChannel {

    //定义成员变量
    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private static final int PORT = 8085;

    /**
     * 构造器
     */
    public GroupServerChannel(){
        try {
            //初始化selector
            selector = Selector.open();
            //初始化serverSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            //绑定端口号
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            //selector注册，监听事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端监听方法
     */
    public void serverListen(){
        try {
            while (true){
                //每次扫描持续两秒
                int res = selector.select(2000);
                if (res > 0) {
                    //拿到set集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        //通道连接成功
                        if (selectionKey.isAcceptable()) {
                            //拿到客户端连接
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            //将拿到的连接注册进selector中
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线了");
                        }
                        //通道可读
                        if (selectionKey.isReadable()) {
                            readDate(selectionKey);
                        }
                    }
                    //移除当前selectionKey
                    iterator.remove();
                } else {
                    //System.out.println("等待中。。。");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 读取数据
     * @param selectionKey
     */
    private void readDate(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            //得到客户端连接channel
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //读取数据到byteBuffer中
            int read = socketChannel.read(byteBuffer);
            //如果读取到就转发给其他客户端
            if (read > 0) {
                //打印消息
                String msg = new String(byteBuffer.array());
                System.out.println("from客户端：" + msg);
                //转发消息
                sendOtherChannel(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                //取消注册
                selectionKey.cancel();
                //关闭通道
                socketChannel.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }

    /**
     * 转发消息
     * @param msg 消息内容
     * @param slefChannel 发消息的客户端，应被排除
     */
    private void sendOtherChannel(String msg, SocketChannel slefChannel) throws IOException {
        System.out.println("转发消息至其他客户端...");
        for (SelectionKey selectionKey:selector.keys()) {
            Channel targetChannel = selectionKey.channel();

            if (targetChannel instanceof SocketChannel && targetChannel != slefChannel) {
                //拿到其他的客户端连接
                SocketChannel dest = (SocketChannel) targetChannel;
                //写入channel
                dest.write(ByteBuffer.wrap(msg.getBytes()));
            }
        }
    }

    public static void main(String[] args) {
        GroupServerChannel groupServerChannel = new GroupServerChannel();
        groupServerChannel.serverListen();
    }
}
