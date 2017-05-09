import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lotus on 2017/5/5.
 */
public class NettyRemoteClient extends AbstractRemote {
    private int port;
    private String host;
    SocketChannel socketChannel;
    private static final EventExecutorGroup group = new DefaultEventExecutorGroup(2);
    public NettyRemoteClient(int port, String host) throws InterruptedException {
        this.port = port;
        this.host = host;
    }
    public void clientStart() throws InterruptedException {
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(eventLoopGroup);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.remoteAddress(host,port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(group);
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                socketChannel.pipeline().addLast(new IdleStateHandler(0,0,120));
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future =bootstrap.connect(host,port).sync();
        if (future.isSuccess()) {
            socketChannel = (SocketChannel)future.channel();
            System.out.println("connect server successfully---------");
        }
    }

    private class NettyClientHandler extends SimpleChannelInboundHandler<RemoteCommand> {

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
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, RemoteCommand command) throws Exception {
            processMessageReceived(channelHandlerContext, command);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

            System.err.println("in client exceptionCaught.");
            super.exceptionCaught(ctx, cause);
        }

    }

    public static void main(String[] args) {
        try {
            NettyRemoteClient remoteClient=new NettyRemoteClient(9999,"localhost");
            remoteClient.clientStart();

            RequestRemoteCommand request = new RequestRemoteCommand();
            request.setClientId("001");
            List<MessageType> typeList = new ArrayList<MessageType>();
            typeList.add(MessageType.SYSTEM_MESSAGE);
            typeList.add(MessageType.USER_MESSAGE);
            request.setMessageTypeList(typeList);
            remoteClient.socketChannel.writeAndFlush(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
