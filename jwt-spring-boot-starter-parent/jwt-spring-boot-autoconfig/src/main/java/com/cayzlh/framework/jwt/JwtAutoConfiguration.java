package com.cayzlh.framework.jwt;

import com.cayzlh.framework.jwt.adapter.JwtInterceptorHandlerAdapter;
import com.cayzlh.framework.jwt.aop.RequiresAuthenticationAnnotationHandler;
import com.cayzlh.framework.jwt.aop.RequiresGuestAnnotationHandler;
import com.cayzlh.framework.jwt.aop.RequiresPermissionsAnnotationHandler;
import com.cayzlh.framework.jwt.aop.RequiresRolesAnnotationHandler;
import com.cayzlh.framework.jwt.aop.RequiresUserAnnotationHandler;
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
