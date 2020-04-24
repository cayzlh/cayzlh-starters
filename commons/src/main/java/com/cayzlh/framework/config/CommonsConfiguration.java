package com.cayzlh.framework.config;

import com.cayzlh.framework.util.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2020/01/16.
 */
@Configuration
public class CommonsConfiguration {

    @Bean
//    @ConditionalOnBean(StringRedisTemplate.class)
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtil(stringRedisTemplate);
    }

}
