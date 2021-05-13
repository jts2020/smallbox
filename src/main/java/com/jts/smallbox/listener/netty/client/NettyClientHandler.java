package com.jts.smallbox.listener.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jts
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Netty client active..");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.printf("rec msg:[%s]%n", msg);
        String res = String.format("%s by Client", msg);
        try {
            ctx.writeAndFlush(res);
            System.out.printf("sed msg:[%s]%n", res);
        } finally {
            ctx.channel().close();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
