package com.gyg.controller;

import com.gyg.netty.handler.ServerInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author by gyg
 * @date 2021/11/28 22:39
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/")
public class TestController {

    private final ServerInboundHandler serverInboundHandler;

    public TestController(ServerInboundHandler serverInboundHandler) {
        this.serverInboundHandler = serverInboundHandler;
    }

    @PostMapping
    public void write(@RequestParam String message, @RequestParam String channelId) {
        log.info("收到写入命令");
        serverInboundHandler.channelWrite(channelId, message);
    }
}
