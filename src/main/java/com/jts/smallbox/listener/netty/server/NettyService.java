package com.jts.smallbox.listener.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author jts
 */
@Slf4j
@Service
public class NettyService implements ApplicationListener<ApplicationReadyEvent> {

    private final ServerChannelInitializer serverChannelInitializer;
    /**
     * 主线程组
     */
    private final EventLoopGroup bossGroup;
    /**
     * 工作线程组
     */
    private final EventLoopGroup workGroup;

    @Value("${netty.server.hostname:127.0.0.1}")
    private String hostname;

    @Value("${netty.server.port:8090}")
    private Integer port;

    public NettyService(ServerChannelInitializer serverChannelInitializer) {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup(10);
        log.debug("{} nettyService",Thread.currentThread().getName());
        this.serverChannelInitializer = serverChannelInitializer;
    }

    public void startNettyServer() {
        InetSocketAddress socketAddress = new InetSocketAddress(hostname, port);

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(serverChannelInitializer)
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                //两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        //绑定端口,开始接收进来的连接
        try {
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            log.info("Server started port:{}", socketAddress.getPort());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        } finally {
            shutdownServer();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        new Thread(() -> startNettyServer(),"netty:"+port).start();
    }

    public void shutdownServer(){
        log.debug("{} PreDestroy shutdownNettyServer",Thread.currentThread().getName());
        //关闭主线程组
        if(Objects.nonNull(bossGroup)){
            bossGroup.shutdownGracefully();
            log.debug("{} shutdownNettyServer bossGroup",Thread.currentThread().getName());
        }
        //关闭工作线程组
        if(Objects.nonNull(workGroup)){
            workGroup.shutdownGracefully();
            log.debug("{} shutdownNettyServer workGroup",Thread.currentThread().getName());
        }
    }

    @PreDestroy
    public void preDestroy(){
        shutdownServer();
    }
}
