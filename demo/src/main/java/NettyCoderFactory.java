import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by lotus on 2017/5/9.
 */
public class NettyCoderFactory {

    private class NettyEncoder extends MessageToByteEncoder<RemoteCommand> {

        private Kryo kryo = new Kryo();

        @Override
        protected void encode(ChannelHandlerContext ctx, RemoteCommand remoteCommand, ByteBuf out) throws Exception {

            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Output output = new Output(bos);
                kryo.writeObject(output, remoteCommand);
                output.flush();

                byte[] body = bos.toByteArray();
                int dataLength = body.length;
                out.writeInt(dataLength);
                out.writeBytes(body);
            } catch (KryoException e) {
                e.printStackTrace();
                ctx.close();
            }
        }

    }

    private class NettyDecoder extends ByteToMessageDecoder {

        private Kryo kryo = new Kryo();

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

            int dataLength = in.readInt();
            if (dataLength < 0) {
                ctx.close();
            }

            byte[] body = new byte[dataLength];
            in.readBytes(body);
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(body);
                Input input = new Input(bis);

                Object o = kryo.readObject(input, RemoteCommand.class);
                out.add(o);
            } catch (KryoException e) {
                e.printStackTrace();
                ctx.close();
            }
        }
    }

    public ChannelHandler getEncoder() {
        return new NettyEncoder();
    }

    public ChannelHandler getDecoder() {
        return new NettyDecoder();
    }
}
