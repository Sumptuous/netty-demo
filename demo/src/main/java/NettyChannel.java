import io.netty.channel.socket.SocketChannel;

/**
 * Created by Administrator on 2017/5/9.
 */
public class NettyChannel {

    private RemoteCommand remoteCommand;

    private SocketChannel socketChannel;

    public RemoteCommand getRemoteCommand() {
        return remoteCommand;
    }

    public void setRemoteCommand(RemoteCommand remoteCommand) {
        this.remoteCommand = remoteCommand;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
}
