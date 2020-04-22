package com.cayzlh.framework.jwt;

import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.jwt.config.ShiroProperties;
import com.cayzlh.framework.jwt.shiro.cache.LoginRedisService;
import com.cayzlh.framework.jwt.shiro.cache.impl.LoginRedisServiceImpl;
import com.cayzlh.framework.jwt.shiro.service.ShiroLoginService;
import com.cayzlh.framework.jwt.shiro.service.impl.ShiroLoginServiceImpl;
import com.cayzlh.framework.jwt.util.JwtTokenUtil;
import com.cayzlh.framework.jwt.util.JwtUtil;
import com.cayzlh.framework.jwt.util.SaltUtil;
import com.cayzlh.framework.jwt.util.ShiroUtil;
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
    public ShiroProperties shiroProperties(){return new ShiroProperties();}

    @Bean
    public JwtUtil jwtUtil(JwtProperties jwtProperties) {
        return new JwtUtil(jwtProperties);
    }

    @Bean
    public SaltUtil saltUtil(JwtProperties jwtProperties) {
        return new SaltUtil(jwtProperties);
    }

    @Bean
    public ShiroUtil shiroUtil(ShiroProperties shiroProperties) {
        return new ShiroUtil(shiroProperties);
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(JwtProperties jwtProperties) {
        return new JwtTokenUtil(jwtProperties);
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
