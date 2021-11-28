package com.gyg.netty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author by gyg
 * @date 2021/11/28 21:59
 * @description
 */
@Configuration
@ConfigurationProperties(prefix = "netty")
@Data
public class NettyProperty {
    private Integer port;
}
