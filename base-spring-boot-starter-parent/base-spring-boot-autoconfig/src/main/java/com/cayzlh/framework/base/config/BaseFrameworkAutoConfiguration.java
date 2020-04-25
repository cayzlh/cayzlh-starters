package com.cayzlh.framework.base.config;

import com.cayzlh.framework.base.advice.BaseResponseAdvice;
import com.cayzlh.framework.base.handler.ExceptionsHandler;
import com.cayzlh.framework.base.handler.GeneralInterceptorHandler;
import com.cayzlh.framework.base.log.aop.BaseLogAop;
import com.cayzlh.framework.base.log.bean.OperationInfo;
import com.cayzlh.framework.base.log.bean.RequestInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@Configuration
@ConditionalOnClass({BaseResponseAdvice.class, ExceptionsHandler.class,
        GeneralInterceptorHandler.class, InterceptorConfig.class})
@EnableConfigurationProperties(BaseProperties.class)
public class BaseFrameworkAutoConfiguration {

    @Bean
    public BaseProperties baseProperties() {
        return new BaseProperties();
    }

    @Bean
    public BaseResponseAdvice baseResponseAdvice() {
        return new BaseResponseAdvice(baseProperties());
    }

    @Bean
    public ExceptionsHandler exceptionsHandler() {
        return new ExceptionsHandler();
    }

    @Bean
    public GeneralInterceptorHandler generalInterceptorHandler() {
        return new GeneralInterceptorHandler();
    }

    @Bean
    public InterceptorConfig interceptorConfig() {
        return new InterceptorConfig();
    }

}
