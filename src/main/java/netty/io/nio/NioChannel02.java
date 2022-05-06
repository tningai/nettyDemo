package netty.io.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 从本地路径中读取文件内容并打印
 * 1、从本地文件中读取内容至输入流
 * 2、读出channel通道中
 * 3、读入至byteBuffer并输出
 */
public class NioChannel02 {
    public static void main(String[] args) {
        FileInputStream fileInputStream = null;
        try {
            //将文件内容读入输入流
            fileInputStream = new FileInputStream("hello01.txt");

            //读入channel中
            FileChannel channel = fileInputStream.getChannel();

            //新建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            //从channel通道中读取数据写入缓冲区中
            int read = channel.read(byteBuffer);
            System.out.println("read:" + read);

            System.out.println(new String(byteBuffer.array()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
