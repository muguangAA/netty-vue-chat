package com.netty.chat.chatRoom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Slf4j
public class NettyServer{

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    @Value("${netty.tcp.server.port}")
    private int port;

    @PostConstruct
    public void start() throws InterruptedException {

        // 创建 bootstrap 对象，用于 Netty Server启动
        ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 服务端 accept 队列的大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new WSServerInitializer());
            // 服务器异步创建绑定
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("netty server 启动完毕,启动端口为："+ port);
            if (future.isSuccess()) {
                channel = future.channel();
                log.info("[start][Netty Server 启动在 {} 端口]", port);
            }
    }

    @PreDestroy
    public void shutdown() {
        // 关闭 Netty Server
        if (channel != null) {
            channel.close();
        }
        // 优雅关闭两个 EventLoopGroup 对象
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
