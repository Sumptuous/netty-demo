import java.util.ArrayList;
import java.util.List;

/**
 * Created by lotus on 2017/5/5.
 */
public class NettyClient {

    public static void main(String[] args) {
        try {
            NettyRemoteClient remoteClient=new NettyRemoteClient(9999,"localhost");
            remoteClient.clientStart();

            RequestRemoteCommand request = new RequestRemoteCommand();
            request.setClientId("002");
            List<MessageType> typeList = new ArrayList<MessageType>();
            typeList.add(MessageType.SYSTEM_MESSAGE);
            request.setMessageTypeList(typeList);
            remoteClient.socketChannel.writeAndFlush(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
