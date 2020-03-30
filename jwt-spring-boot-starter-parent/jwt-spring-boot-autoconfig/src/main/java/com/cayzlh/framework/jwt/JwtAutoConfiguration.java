package com.cayzlh.framework.jwt;

import com.cayzlh.framework.jwt.adapter.JwtInterceptorHandlerAdapter;
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
    public JwtTokenUtil tokenUtil() {
        return new JwtTokenUtil(jwtProperties());
    }

    @Bean
    public JwtInterceptorHandlerAdapter jwtInterceptorHandler(){
        return new JwtInterceptorHandlerAdapter();
    }

    @Bean
    public JwtInterceptorConfig jwtInterceptorConfig() {
        return new JwtInterceptorConfig();
    }

}
