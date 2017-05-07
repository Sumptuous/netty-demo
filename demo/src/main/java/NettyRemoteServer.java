import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by lotus on 2017/5/5.
 */
public class NettyRemoteServer extends AbstractRemote {
    private int port;
    private ServerSocketChannel serverSocketChannel;
    private static final EventExecutorGroup group = new DefaultEventExecutorGroup(12);
    public NettyRemoteServer(int port) throws InterruptedException {
        this.port = port;
    }

    public void serverStart() throws InterruptedException {
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();
        ServerBootstrap bootstrap=new ServerBootstrap();
        bootstrap.group(boss,worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(new IdleStateHandler(10,20,0));
                p.addLast(new ObjectEncoder());
                p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                p.addLast(new NettyServerHandler());
                p.addLast(group);
            }
        });
        ChannelFuture f= bootstrap.bind(port).sync();
        if(f.isSuccess()){
            serverSocketChannel = (ServerSocketChannel)f.channel();
            System.out.println("server start---------------");
        }
    }

    private class NettyServerHandler extends SimpleChannelInboundHandler<RemoteCommand> {

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state().equals(IdleState.ALL_IDLE)) {
                    ctx.close();
                }
            }
            ctx.fireUserEventTriggered(evt);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.err.println("in channelInactive.");
            NettyChannelMap.remove((SocketChannel)ctx.channel());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, RemoteCommand command) throws Exception {
            processMessageReceived(channelHandlerContext, command);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

            System.err.println("in here has an error.");
            NettyChannelMap.remove((SocketChannel)ctx.channel());
            super.exceptionCaught(ctx, cause);
            System.err.println("channel is exception over. (SocketChannel)ctx.channel()=" + ctx.channel());
        }
    }

    public static void main(String[] args) {
        try {
            NettyRemoteServer remoteServer = new NettyRemoteServer(9999);
            remoteServer.serverStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
