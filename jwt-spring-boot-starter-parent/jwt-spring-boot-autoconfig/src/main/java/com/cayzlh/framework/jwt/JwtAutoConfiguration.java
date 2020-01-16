package com.cayzlh.framework.jwt;

import com.cayzlh.framework.jwt.adapter.JwtInterceptorHandlerAdapter;
import com.cayzlh.framework.jwt.aop.RequiresAuthenticationAnnotationHandler;
import com.cayzlh.framework.jwt.aop.RequiresGuestAnnotationHandler;
import com.cayzlh.framework.jwt.aop.RequiresPermissionsAnnotationHandler;
import com.cayzlh.framework.jwt.aop.RequiresRolesAnnotationHandler;
import com.cayzlh.framework.jwt.aop.RequiresUserAnnotationHandler;
import com.cayzlh.framework.jwt.util.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAutoConfiguration {

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public JwtTokenUtil tokenUtil() {
        return new JwtTokenUtil(jwtProperties());
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtil(stringRedisTemplate);
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
    public RequiresAuthenticationAnnotationHandler RequiresAuthenticationAnnotationHandler() {
        return new RequiresAuthenticationAnnotationHandler(tokenUtil());
    }

    @Bean
    public RequiresGuestAnnotationHandler requiresGuestAnnotationHandler() {
        return new RequiresGuestAnnotationHandler();
    }

    @Bean
    public RequiresRolesAnnotationHandler requiresRolesAnnotationHandler() {
        return new RequiresRolesAnnotationHandler();
    }

    @Bean
    public RequiresPermissionsAnnotationHandler requiresPermissionsAnnotationHandler() {
        return new RequiresPermissionsAnnotationHandler();
    }

    @Bean
    public RequiresUserAnnotationHandler requiresUserAnnotationHandler() {
        return new RequiresUserAnnotationHandler();
    }

}
