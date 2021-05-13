package com.jts.smallbox.listener.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author jts
 */
@Slf4j
public class MsgDecoder extends ByteToMessageDecoder {

    private final Charset charset;

    public MsgDecoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        log.info("Exec MsgDecoder.decode");
        int msgLength = in.readInt();
        String body = null;
        if (msgLength > 0) {
            ByteBuf buf = in.readBytes(msgLength);
            byte[] reqByte = new byte[buf.readableBytes()];
            buf.readBytes(reqByte);
            body = new String(reqByte, charset);
        }
        out.add(body);
    }
}
