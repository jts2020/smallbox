package com.jts.smallbox.listener.netty.client;

import com.jts.smallbox.listener.netty.coder.MsgDecoder;
import com.jts.smallbox.listener.netty.coder.MsgEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jts
 */
@Slf4j
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        System.out.println("Exec ClientChannelInitializer.initChannel");
        ChannelPipeline pipe = socketChannel.pipeline();
        pipe.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
        pipe.addLast(new MsgDecoder(CharsetUtil.UTF_8));
        pipe.addLast(new MsgEncoder(CharsetUtil.UTF_8));
        pipe.addLast(new NettyClientHandler());
    }
}
