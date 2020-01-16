package com.cayzlh.framework.distributedlock.redis;

import com.cayzlh.framework.distributedlock.redis.annotations.RedisLock;
import com.cayzlh.framework.distributedlock.redis.annotations.RedisLock.LockFailAction;
import com.cayzlh.framework.distributedlock.redis.lock.DistributedLock;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
@ConditionalOnClass(DistributedLock.class)
@AutoConfigureAfter(RedisDistributedLockAutoConfiguration.class)
@Slf4j
public class RedisDistributedLockAspectConfiguration {

	private final DistributedLock distributedLock;
	
	private ExpressionParser parser = new SpelExpressionParser();

	private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

	@Autowired
	public RedisDistributedLockAspectConfiguration(
			DistributedLock distributedLock) {
		this.distributedLock = distributedLock;
	}

	@Pointcut("@annotation(com.cayzlh.framework.distributedlock.redis.annotations.RedisLock)")
	private void lockPoint(){
		
	}
	
	@Around("lockPoint()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		RedisLock redisLock = method.getAnnotation(RedisLock.class);
		String key = getLogKey(redisLock, pjp, method);
		Object[] args = pjp.getArgs();
		key = parse(key, method, args);
		
		
		int retryTimes = redisLock.action().equals(LockFailAction.CONTINUE) ? redisLock.retryTimes() : 0;
		boolean lock = distributedLock.lock(key, redisLock.keepMills(), retryTimes, redisLock.sleepMills());
		if(!lock) {
			log.debug("get lock failed : {}" , key);
			return null;
		}
		
		//得到锁,执行方法，释放锁
		log.debug("get lock success : {}" , key);
		try {
			return pjp.proceed();
		} catch (Exception e) {
			log.error("execute locked method occured an exception", e);
			throw e;
		} finally {
			boolean releaseResult = distributedLock.releaseLock(key);
			log.debug("release lock : " + key + (releaseResult ? " success" : " failed"));
		}
	}

	private String getLogKey(RedisLock redisLock, ProceedingJoinPoint pjp, Method method) {
		String key = redisLock.key();
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
