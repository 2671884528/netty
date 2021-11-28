package com.gyg.netty.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author by gyg
 * @date 2021/11/28 22:26
 * @description
 */
@Aspect
@Slf4j
@Component
public class MDCAspect {

    @Pointcut("@annotation(com.gyg.netty.aop.MDCLog)")
    public void annotationPointcut() {
    }

    @Before("annotationPointcut()")
    public void aspectBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MDCLog mdcLog = method.getAnnotation(MDCLog.class);
        String name = mdcLog.name();
        // 【0】这里会打印上一次traceId
        log.info("进入 before 拦截 name:{}", name);
        // 处理切入业务【这里做日志 traceId 生成】,根据日志框架格式插入相关的key
        // 这里是slf4j，key采用X-B3-TraceId，在日志配置中可以看到相关配置
        // 这里最好再配置一个 After用来删除 traceId
        // 【每次用完即删，但是因为覆盖问题，也可以不删除，不删除【0】的位置会出现上一次traceId】
        if (StringUtils.isEmpty(MDC.get("X-B3-TraceId"))) {
            MDC.put("X-B3-TraceId", UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        }
    }

    @After("annotationPointcut()")
    public void aspectAfter(JoinPoint joinPoint) {
        log.info("after 触发");
        MDC.clear();
    }

}
