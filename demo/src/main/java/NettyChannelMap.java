import io.netty.channel.socket.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/5/5.
 */
public class NettyChannelMap {
    private static Map<String, NettyChannel> map = new ConcurrentHashMap<String, NettyChannel>();
    public static void add(String clientId, NettyChannel nettyChannel){
        map.put(clientId, nettyChannel);
    }
    public static NettyChannel get(String clientId){
        return map.get(clientId);
    }
    public static Map<String, NettyChannel> getAll() {
        return map;
    }
    public static void remove(SocketChannel socketChannel){
        Iterator<Map.Entry<String, NettyChannel>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, NettyChannel> entry = iterator.next();
            if (entry.getValue().getSocketChannel().equals(socketChannel)) {
                iterator.remove();
            }
        }
    }
}
