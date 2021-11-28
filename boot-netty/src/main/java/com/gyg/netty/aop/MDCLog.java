package com.gyg.netty.aop;

import java.lang.annotation.*;

/**
 * @author by gyg
 * @date 2021/11/28 22:24
 * @description
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface MDCLog {
    String name();
}
