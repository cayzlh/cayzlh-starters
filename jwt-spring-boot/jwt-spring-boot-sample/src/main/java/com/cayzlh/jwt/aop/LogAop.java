/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.aop;

import com.cayzlh.framework.base.log.aop.BaseLogAop;
import com.cayzlh.framework.base.log.bean.OperationInfo;
import com.cayzlh.framework.base.log.bean.RequestInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Ant丶
 * @date 2020-04-26.
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(value = {"cayzlh.framework.base.log.enable"}, matchIfMissing = true)
public class LogAop extends BaseLogAop {

    private static final String POINTCUT =
            "execution(public * com.cayzlh.jwt.controller.*.*(..))";

    @Pointcut(POINTCUT)
    private void log() {

    }

    @Around("log()")
    @Override
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.handle(joinPoint);
    }

    @AfterThrowing(pointcut = "log()", throwing = "exception")
    @Override
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        super.handleAfterThrowing(exception);
    }

    @Override
    protected void setRequestId(RequestInfo requestInfo) {
        super.handleRequestId(requestInfo);
    }

    @Override
    protected void getRequestInfo(RequestInfo requestInfo) {
        super.handleRequestInfo(requestInfo);
    }

    @Override
    protected void getResponseResult(Object result) {
        super.handleResponseResult(result);
    }

    @Override
    protected void finish(RequestInfo requestInfo, OperationInfo operationInfo, Object result,
            Exception exception) {
        log.debug("do log aop finish..");
        // 如果需要将请求信息保存到数据库之类的，就在这个地方写
    }
}
