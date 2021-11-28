package com.gyg.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

/**
 * @author by gyg
 * @date 2021/11/28 22:05
 * @description
 */
@Component
public class NettyChannelHandler extends ChannelInitializer<NioSocketChannel> {

    private final ServerInboundHandler serverInboundHandler;

    public NettyChannelHandler(ServerInboundHandler serverInboundHandler) {
        this.serverInboundHandler = serverInboundHandler;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        // 识别换行符为一个消息【具体需要和客户端定专有的协议，这里是通用】
//        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        ch.pipeline().addLast(new LoggingHandler());
        ch.pipeline().addLast(serverInboundHandler);
    }
}
