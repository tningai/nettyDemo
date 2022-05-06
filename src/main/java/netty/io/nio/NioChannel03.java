package netty.io.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用一个buffer完成文件的拷贝
 * 1、将文件内容写入channel
 * 2、再读入byteBuffer中
 * 3、再写入channel中
 * 4、输出至另一个文件
 */
public class NioChannel03 {
    public static void main(String[] args) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            //新建一个缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(5);

            fileInputStream = new FileInputStream("hello01.txt");
            //输入流创建channel
            FileChannel inChannel = fileInputStream.getChannel();

            inChannel.size();

            fileOutputStream = new FileOutputStream("hello02.txt");
            //输出流创建channel
            FileChannel outChannel = fileOutputStream.getChannel();

            //从管道中读取数据写入缓存区中
            while (true) {
                //缓冲区复位，经过下方的写入操作后，position实际上已经与limit相等，执行clear方法，使position复位到0
                byteBuffer.clear();
                int read = inChannel.read(byteBuffer);
                if (read == -1) {
                    break;
                }
                System.out.println("read:" + read);

                //缓冲区读写转换
                byteBuffer.flip();
                //从缓存区中读数据写入channel管道中
                int write = outChannel.write(byteBuffer);
                System.out.println("write:" + write);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
