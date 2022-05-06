package netty.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 使用nio原生方法拷贝文件
 */
public class NioChannle04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("hello01.txt");

        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("hello04.txt");

        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        //long l = fileOutputStreamChannel.transferFrom(fileInputStreamChannel, 0, fileInputStreamChannel.size());
        long l = fileInputStreamChannel.transferTo(0, fileInputStreamChannel.size(), fileOutputStreamChannel);
        System.out.println("l:" + l);

        fileInputStreamChannel.close();

        fileOutputStreamChannel.close();

        fileInputStream.close();

        fileOutputStream.close();
    }
}
