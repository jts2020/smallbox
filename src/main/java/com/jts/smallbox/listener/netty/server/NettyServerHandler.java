package com.jts.smallbox.listener.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author jts
 */
@Slf4j
@Scope("prototype")
@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Channel active......");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("NettyServerHandler [{}] rec msg:[{}]", this, msg);
        String res = String.format("%s by Server", msg);
        ctx.writeAndFlush(res);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
