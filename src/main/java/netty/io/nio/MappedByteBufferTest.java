package netty.io.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存中直接修改文件内容，不用操作系统再拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("hello01.txt","rw");

        FileChannel channel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,0,22);

        mappedByteBuffer.put(0,(byte) 'H');
        mappedByteBuffer.put("哈".getBytes(),0,3);

        randomAccessFile.close();
    }
}
