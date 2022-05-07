package netty.io.group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupClientChannel {

    private Selector selector;

    private SocketChannel socketChannel;

    private String userName;

    private final String IP = "127.0.0.1";

    private final int PORT = 8085;

    /**
     * 客户端构造器
     */
    public GroupClientChannel() {
        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open(new InetSocketAddress(IP, PORT));
            socketChannel.configureBlocking(false);

            socketChannel.register(selector, SelectionKey.OP_READ);

            userName = socketChannel.getRemoteAddress().toString().substring(1);

            System.out.println(userName + "客户端启动");
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 发送消息
     * @param info
     */
    public void sendInfo(String info){
        info = userName + " say:" + info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void readInfo(){
        try {
            int channels = selector.select();
            if(channels > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        //从通道中读取数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        //输出
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());
                    }
                }
                //移除当前selectionKey
                iterator.remove();
            } else {
                //System.out.println("没有可用的通道");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupClientChannel groupClientChannel = new GroupClientChannel();

        //客户端读取数据
        new Thread(){
            public void run(){
                while (true) {
                    groupClientChannel.readInfo();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //客户端输入数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.next();
            groupClientChannel.sendInfo(s);
        }
    }
}
