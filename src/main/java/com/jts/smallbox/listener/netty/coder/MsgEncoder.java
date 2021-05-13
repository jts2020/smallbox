package com.jts.smallbox.listener.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author jts
 */
@Slf4j
public class MsgEncoder extends MessageToByteEncoder<String> {

    private final Charset charset;

    public MsgEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) {
        log.info("Exec MsgEncoder.encode");
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        byte[] reqBytes = msg.getBytes(charset);
        out.writeInt(reqBytes.length);
        out.writeBytes(reqBytes);
    }
}
