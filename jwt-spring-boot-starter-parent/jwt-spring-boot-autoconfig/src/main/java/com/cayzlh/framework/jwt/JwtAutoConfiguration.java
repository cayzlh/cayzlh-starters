package com.cayzlh.framework.jwt;

import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.jwt.shiro.cache.LoginRedisService;
import com.cayzlh.framework.jwt.shiro.cache.impl.LoginRedisServiceImpl;
import com.cayzlh.framework.jwt.util.JwtUtil;
import com.cayzlh.framework.util.RedisUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAutoConfiguration {

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public JwtUtil tokenUtil() {
        return new JwtUtil(jwtProperties());
    }

    @Bean
    public LoginRedisService loginRedisService(JwtProperties jwtProperties, RedisUtil redisUtil) {
        return new LoginRedisServiceImpl(jwtProperties, redisUtil);
    }


}
