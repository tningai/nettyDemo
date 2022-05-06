package netty.io.nio;

import java.nio.IntBuffer;

/**
 * 理解buffer缓冲区
 */
public class BasicBuffer {
    public static void main(String[] args) {
        //新建IntBuffer，容量为5，每个元素都为int型，四个字节
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //循环放入元素，小于对象初始化长度
        for (int i=0;i<intBuffer.capacity();i++) {
            intBuffer.put((i+1)*2);
        }

        //双向，读写转化
        intBuffer.flip();

        //输出内容
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
