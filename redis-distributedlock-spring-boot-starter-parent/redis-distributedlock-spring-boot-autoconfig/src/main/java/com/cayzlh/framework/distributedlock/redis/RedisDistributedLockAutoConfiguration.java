package com.cayzlh.framework.distributedlock.redis;


import com.cayzlh.framework.distributedlock.redis.lock.DistributedLock;
import com.cayzlh.framework.distributedlock.redis.lock.RedisDistributedLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisDistributedLockAutoConfiguration {
	
	@Bean
	@ConditionalOnBean(RedisTemplate.class)
	public DistributedLock distributedLock(RedisTemplate<Object, Object> redisTemplate){
		return new RedisDistributedLock(redisTemplate);
	}
	
}
