package netty.io.netty.sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 客户端
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端的事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try{
            //客户端自动对象，不是serverBootstrap
            Bootstrap bootstrap = new Bootstrap();

            //配置参数
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());//加入handler处理器
                        }
                    });
            System.out.println("client is ready");

            //启动客户端建立连接
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8001).sync();
            //对channel通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
