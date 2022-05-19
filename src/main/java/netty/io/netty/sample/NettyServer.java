package netty.io.netty.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //服务端新建boss和worker循环事件组，boss负责轮询accept方法，监听和绑定客户端socket连接，worker负责具体的handler处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //服务器端自动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //具体参数配置
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)//服务器端配置NioServerSocketChannel通道
                    //.handler()
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列等待个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象，匿名对象
                        //给pipeline设置handler处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("server is ready");

            //绑定端口号并设置同步监听，返回一个channelFuture
            ChannelFuture channelFuture = bootstrap.bind(8001).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
