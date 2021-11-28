package com.gyg.netty;

import com.gyg.netty.handler.NettyChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author by gyg
 * @date 2021/11/28 21:58
 * @description
 */
@Slf4j
@Component
public class NettyServer implements ApplicationRunner {

    private final NettyChannelHandler nettyChannelHandler;
    private final NettyProperty nettyProperty;

    public NettyServer(NettyChannelHandler nettyChannelHandler, NettyProperty nettyProperty) {
        this.nettyChannelHandler = nettyChannelHandler;
        this.nettyProperty = nettyProperty;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        // 根据CPU算默认值
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(boss, work);
        sb.channel(NioServerSocketChannel.class);
        sb.childHandler(nettyChannelHandler);
        ChannelFuture future = sb.bind(nettyProperty.getPort());
        future.sync();
        log.info("spring 启动 netty..................");
    }
}
