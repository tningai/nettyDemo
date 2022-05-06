package netty.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 阻塞io实例
 */
public class BioServer {

    public static void main(String[] args) throws IOException {
        //新建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        //新建serverSocket，并绑定端口号
        ServerSocket serverSocket = new ServerSocket(8083);

        //监听端口号并处理客户端的数据
        while (true) {
            //接受客户端的socket请求
            final Socket socket = serverSocket.accept();
            //每次有了新的socket请求，创建一个新的thread
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }

    }

    /**
     * 处理客户端连接的数据
     * @param socket 客户端连接的socket
     */
    public static void handler(Socket socket) {
        InputStream inputStream = null;
        try {
            System.out.println(Thread.currentThread().getId() + ";" + Thread.currentThread().getName());
            //创建缓冲区存放读取的字节流
            byte[] bytes = new byte[1024];
            //从socket中读取输入流
            inputStream = socket.getInputStream();
            //从流中读取字节到缓冲区中
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //未读取完，输出
                    System.out.println(new String(bytes,0,read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭socket
            try{
                if(inputStream != null) {
                    inputStream.close();
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
