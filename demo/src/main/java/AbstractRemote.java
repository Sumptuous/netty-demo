import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by lotus on 2017/5/5.
 */
public class AbstractRemote {

    public void processMessageReceived(ChannelHandlerContext channelHandlerContext, RemoteCommand command) {
        switch (command.getType()) {
            case REQUEST_COMMAND:
                processRequestCommand(channelHandlerContext, command);
                break;
            case RESPONSE_COMMAND:
                processResponseCommand(channelHandlerContext, command);
                break;
            default:
                break;
        }
    }

    private void processResponseCommand(ChannelHandlerContext channelHandlerContext, RemoteCommand response) {
        System.out.println("message: " + response.getMessage());
        System.out.println("channelId: " + channelHandlerContext.channel().id());
        System.out.println("------******------\n");
        ReferenceCountUtil.release(response);
    }

    private void processRequestCommand(ChannelHandlerContext channelHandlerContext, RemoteCommand request) {
        NettyChannel nettyChannel = new NettyChannel();
        nettyChannel.setSocketChannel((SocketChannel) channelHandlerContext.channel());
        nettyChannel.setRemoteCommand(request);
        NettyChannelMap.add(request.getClientId(), nettyChannel);
    }
}
