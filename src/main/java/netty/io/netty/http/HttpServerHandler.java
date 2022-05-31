package netty.io.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 自定义一个业务处理的handler
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是否是httpRequest类型的
        if (msg instanceof HttpRequest) {
            //msg类型
            System.out.println("msg class:" + msg.getClass());
            //客户端地址
            System.out.println("http client address:" + ctx.channel().remoteAddress());

            //浏览器客户端重复请求拦截
            /*URI uri = new URI(((HttpRequest) msg).uri());
            String path = uri.getPath();
            System.out.println("uri:" + path);
            if ("/favicon.ico".equals(path)) {
                System.out.println("图标文件，不做请求，拦截");
                return;
            }*/

            //要回复给客户端的信息
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello server,i am server", CharsetUtil.UTF_8);

            //构建一个http的相应对象
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,byteBuf);
            //设置http相应的类型
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());

            //将相应内容写入管道
            ctx.writeAndFlush(response);
        }
    }
}
