import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

// hello world
public class HelloworldHttpServerHandler extends ChannelInboundHandlerAdapter {

    int i = 0;
    /**
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.getClass());
        System.out.println(msg.toString());
        System.out.println(ctx.channel().remoteAddress());
        i++;
        System.out.println(i);

//        if (msg instanceof HttpRequest ) {
//            HttpRequest httpRequest = (HttpRequest) msg;
//        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            String string = content.content().toString(CharsetUtil.UTF_8);
            ByteBuf body = Unpooled.copiedBuffer(string, CharsetUtil.UTF_8);
            DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
            HttpHeaders headers = new DefaultHttpHeaders();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());

            ctx.writeAndFlush(defaultFullHttpResponse)
                    .addListener(ChannelFutureListener.CLOSE);

        }
//        ReferenceCountUtil.release(msg);
//        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
