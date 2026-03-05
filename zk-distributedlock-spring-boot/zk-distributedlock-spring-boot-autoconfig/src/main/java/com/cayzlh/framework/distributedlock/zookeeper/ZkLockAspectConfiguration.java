package com.cayzlh.framework.distributedlock.zookeeper;

import com.cayzlh.framework.distributedlock.DistributedLock;
import com.cayzlh.framework.distributedlock.zookeeper.annotation.ZkLock;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Aspect
@Configuration
@AutoConfigureAfter(ZkLockAutoConfiguration.class)
@Slf4j
public class ZkLockAspectConfiguration {

    private final DistributedLock zookeeperDistributedLock;

    private ExpressionParser parser = new SpelExpressionParser();

    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Autowired
    public ZkLockAspectConfiguration(DistributedLock zookeeperDistributedLock) {
        this.zookeeperDistributedLock = zookeeperDistributedLock;
    }

    @Pointcut("@annotation(com.cayzlh.framework.distributedlock.zookeeper.annotation.ZkLock)")
    private void lockPoint(){

    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        ZkLock zkLock = method.getAnnotation(ZkLock.class);
        String key = getLogKey(zkLock, pjp, method);
        Object[] args = pjp.getArgs();
        key = parse(key, method, args);

        boolean lock = zookeeperDistributedLock.lock(key, zkLock.expire());

        if(!lock) {
            log.debug("get lock failed [{}]", key);
            return null;
        }

        // 得到锁,执行方法，释放锁
        log.debug("get lock success [{}]", key);
        try {
            return pjp.proceed();
        } catch (Exception e) {
            log.error("execute locked method occured an exception", e);
        } finally {
            zookeeperDistributedLock.releaseLock(key);
            log.debug("release lock [{}]", key);
        }
        return null;
    }

    private String getLogKey(ZkLock zkLock, ProceedingJoinPoint pjp, Method method) {
        String key = zkLock.key();
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
}
