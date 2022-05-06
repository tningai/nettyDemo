package netty.io.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 将字符串内容写入本地文件
 * 1、写入缓冲区byteBuffer
 * 2、写入fileChannel通道
 * 3、通过输出流写入到本地文件
 * 注意flip方法，读写转换
 */
public class NioChannel01 {
    public static void main(String[] args) {
        String str = "hello，第一个文件";

        //新建一个byteBuffer，并写入字符串内容
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());

        //buffer读写转化
        byteBuffer.flip();

        //新建channel通道
        FileOutputStream fileOutputStream = null;
        try {
            //文件输出流，输出到本地文件中
            fileOutputStream = new FileOutputStream("hello01.txt");
            //由输出流生成channel通道
            FileChannel channel = fileOutputStream.getChannel();
            //向channel中写入byteBuffer
            int write = channel.write(byteBuffer);

            System.out.println("write:" + write);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
