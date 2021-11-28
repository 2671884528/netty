package com.gyg.netty.service;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

/**
 * <p>具体业务</p>
 *
 * @author by gyg
 * @date 2021/11/28 22:12
 */
@Service
@Slf4j
public class MessageServer {
    public void handleMessage(ByteBuf buf) {
        log.info("业务方处理消息给上层应用 message:{}", buf.toString(Charset.defaultCharset()));
    }
}
