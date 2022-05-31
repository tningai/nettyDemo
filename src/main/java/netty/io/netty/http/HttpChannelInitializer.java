package netty.io.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * httpserver的通道初始化
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //获取管道，并向管道中添加处理器，包括编解码处理器和业务操作处理器

        //获取管道
        ChannelPipeline pipeline = ch.pipeline();

        //向管道中添加编解码器,codec --> code decoder 同时可以做decode和encode，解码和编码
        pipeline.addLast("myHttpServerCodec", new HttpServerCodec());
        //向管道中添加自定义业务处理器
        pipeline.addLast(new HttpServerHandler());
    }
}
