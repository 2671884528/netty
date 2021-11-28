package com.gyg.netty.handler;

import com.gyg.netty.aop.MDCLog;
import com.gyg.netty.service.MessageServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>一般来说 服务器需要对netty进行 发送、接收 操作
 * 【一般业务，写都是及时的消息回复】
 * 【写操作对于服务端来说业务上只需要写入就不需要，再追踪了，相当于，客户端的读，在客户端追踪即可】</p>
 * 【@ChannelHandler.Sharable，缺少这个注解是无法触发多客户端的】
 * @author by gyg
 * @date 2021/11/28 22:06
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
    private final MessageServer messageServer;

    public static Map<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    public ServerInboundHandler(MessageServer messageServer) {
        this.messageServer = messageServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端初始化 name:{},ip:{}", ctx.name(), ctx.channel().remoteAddress().toString());
        String channelId = ctx.channel().id().asShortText();
        if (!CHANNEL_MAP.containsKey(channelId)) {
            CHANNEL_MAP.put(channelId, ctx);
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开连接 name:{},ip:{}", ctx.name(), ctx.channel().remoteAddress().toString());
        String channelId = ctx.channel().id().asShortText();
        CHANNEL_MAP.remove(channelId);
        super.channelInactive(ctx);
    }

    @MDCLog(name = "有消息写入通道")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        log.info("收到消息 data:{}", ((ByteBuf) msg).toString(StandardCharsets.UTF_8));
        // 【0】创建新的buf进行恢复
        ByteBuf respBuf = ctx.alloc().buffer();
        // 【1】处理消息的业务
        messageServer.handleMessage(buf);
        // 【2】可以交给其他服务处理消息，再写入回复【针对需要消息应答的机制，如果不需要应答可忽略】
        respBuf.writeBytes("回复".getBytes(StandardCharsets.UTF_8));
        // 注意：不要使用ctx的writeAndFlush，他不是从尾开始扫描，无法写入
        ctx.channel().writeAndFlush(respBuf);
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("netty 服务器出现异常");
        super.exceptionCaught(ctx, cause);
    }

    /**
     * <p>用于 通知所有的客户端、根据业务需求用，配合 ctx上下文map配套使用</p>
     * @param channelId 通道id
     * @param msg 消息
     */
    @MDCLog(name = "write log")
    public void channelWrite(String channelId, String msg) {
        log.info("发送消息 channelId:{} msg:{}", channelId, msg);
        ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId);
        if (msg != null) {
            ctx.channel().writeAndFlush(msg.getBytes(StandardCharsets.UTF_8));
        }
    }
}
