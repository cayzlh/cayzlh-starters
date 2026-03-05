package com.cayzlh.framework.distributedlock.redisson;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Aspect
@Configuration
@ConditionalOnClass(RedissonClient.class)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@Slf4j
public class RedissonDistributedLockAspectConfiguration {

    private final RedissonClient redissonClient;

    private ExpressionParser parser = new SpelExpressionParser();

    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Autowired
    public RedissonDistributedLockAspectConfiguration(
            RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Pointcut("@annotation(com.cayzlh.framework.distributedlock.redisson.RedissonLock)")
    private void lockPoint(){

    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        String key = getLogKey(redissonLock, pjp, method);
        Object[] args = pjp.getArgs();
        key = parse(key, method, args);

        RLock lock = getLock(key, redissonLock);
        if(!lock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.unit())) {
            log.debug("get lock failed [{}]", key);
            return null;
        }

        //得到锁,执行方法，释放锁
        log.debug("get lock success [{}]", key);
        try {
            return pjp.proceed();
        } catch (Exception e) {
            log.error("execute locked method occured an exception", e);
        } finally {
            lock.unlock();
            log.debug("release lock [{}]", key);
        }
        return null;
    }

    private String getLogKey(RedissonLock redissonLock, ProceedingJoinPoint pjp, Method method) {
        String key = redissonLock.key();
        if (key.isEmpty()) {
            String packageName = pjp.getSignature().getDeclaringTypeName();
            String methodName = method.getName();
            return packageName+"_"+methodName;
        }
        return key;
    }

    private String parse(String key, Method method, Object[] args) {
        String[] params = discoverer.getParameterNames(method);
        if (null == params || params.length == 0 || !key.contains("#")) {
            return key;
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

    private RLock getLock(String key, RedissonLock redissonLock) {
        switch (redissonLock.lockType()) {
            case REENTRANT_LOCK:
                return redissonClient.getLock(key);

            case FAIR_LOCK:
                return redissonClient.getFairLock(key);

            case READ_LOCK:
                return redissonClient.getReadWriteLock(key).readLock();

            case WRITE_LOCK:
                return redissonClient.getReadWriteLock(key).writeLock();

            default:
                throw new RuntimeException("redisson: do not support lock type:" + redissonLock.lockType().name());
        }
    }

}
