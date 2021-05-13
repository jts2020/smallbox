package com.jts.smallbox.listener.netty.server;

import com.jts.smallbox.listener.netty.coder.MsgDecoder;
import com.jts.smallbox.listener.netty.coder.MsgEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author jts
 */
@Slf4j
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {


    private final ApplicationContext context;

    public ServerChannelInitializer(ApplicationContext context) {
        this.context = context;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        log.info("exec ServerChannelInitializer");
        ChannelPipeline pipe = socketChannel.pipeline();
        pipe.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
        pipe.addLast(new MsgDecoder(CharsetUtil.UTF_8));
        pipe.addLast(new MsgEncoder(CharsetUtil.UTF_8));
        pipe.addLast(context.getBean(NettyServerHandler.class));
    }
}
