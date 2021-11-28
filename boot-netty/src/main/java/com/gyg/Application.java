package com.gyg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author by gyg
 * @date 2021/11/28 21:57
 * @description
 */
@SpringBootApplication
@EnableSwagger2
@EnableAspectJAutoProxy(exposeProxy = true)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
