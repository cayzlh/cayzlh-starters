package com.cayzlh.framework.jwt;

import com.cayzlh.framework.jwt.adapter.JwtInterceptorHandlerAdapter;
import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.jwt.shiro.cache.LoginRedisService;
import com.cayzlh.framework.jwt.shiro.cache.impl.LoginRedisServiceImpl;
import com.cayzlh.framework.jwt.shiro.service.ShiroLoginService;
import com.cayzlh.framework.jwt.shiro.service.impl.ShiroLoginServiceImpl;
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
    public JwtInterceptorHandlerAdapter jwtInterceptorHandler(){
        return new JwtInterceptorHandlerAdapter();
    }

    @Bean
    public JwtInterceptorConfig jwtInterceptorConfig() {
        return new JwtInterceptorConfig();
    }

    @Bean
    public LoginRedisService loginRedisService(JwtProperties jwtProperties, RedisUtil redisUtil) {
        return new LoginRedisServiceImpl(jwtProperties, redisUtil);
    }

    @Bean
    public ShiroLoginService shiroLoginService(LoginRedisService loginRedisService, JwtProperties jwtProperties) {
        return new ShiroLoginServiceImpl(loginRedisService, jwtProperties);
    }

}
