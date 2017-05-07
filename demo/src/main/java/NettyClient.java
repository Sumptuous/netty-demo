
import java.util.concurrent.TimeUnit;

/**
 * Created by lotus on 2017/5/5.
 */
public class NettyClient {

    public static void main(String[] args) {
        try {
            NettyRemoteClient remoteClient=new NettyRemoteClient(9999,"localhost");
            remoteClient.clientStart();
            while (true) {
                RequestRemoteCommand command = new RequestRemoteCommand();
                command.setClientId("002");
                remoteClient.socketChannel.writeAndFlush(command);

                TimeUnit.SECONDS.sleep(5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
