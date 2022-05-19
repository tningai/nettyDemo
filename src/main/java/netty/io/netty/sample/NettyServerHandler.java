package netty.io.netty.sample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义服务端的handler方法，处理channel中的数据
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取客户端发送的数据
    /**
     *
     * @param ctx 上下文对象，包含pipeline，channel，地址等
     * @param msg 客户端发送的数据，默认为object，一般都需要解码
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx:" + ctx);

        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("client send msg:" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("client address:" + ctx.channel().remoteAddress());
     }

     //数据读取完毕，并回复客户端确认消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //write and flush 写入缓存并刷新发送
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client",CharsetUtil.UTF_8));
    }

    //异常拦截方法，关闭上下文
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
