import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/5/5.
 */
public class NettyChannelMap {
    private static Map<String,SocketChannel> map=new ConcurrentHashMap<String, SocketChannel>();
    public static void add(String clientId,SocketChannel socketChannel){
        map.put(clientId,socketChannel);
    }
    public static Channel get(String clientId){
        return map.get(clientId);
    }
    public static void remove(SocketChannel socketChannel){
        Iterator<Map.Entry<String, SocketChannel>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if (entry.getValue().equals(socketChannel)) {
                iterator.remove();
            }
        }
    }
}
