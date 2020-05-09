package com.cayzlh.framework.base.config;

import com.cayzlh.framework.base.advice.BaseResponseAdvice;
import com.cayzlh.framework.base.config.properties.BaseProperties;
import com.cayzlh.framework.base.config.properties.SwaggerProperties;
import com.cayzlh.framework.base.handler.ExceptionsHandler;
import com.cayzlh.framework.base.handler.GeneralInterceptorHandler;
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
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
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

    @Bean
    public Swagger2Config swagger2Config(SwaggerProperties swaggerProperties) {
        return new Swagger2Config(swaggerProperties);
    }

    @Bean
    public BaseWebMvcConfig baseWebMvcConfig(BaseProperties baseProperties) {
        return new BaseWebMvcConfig(baseProperties);
    }

    @Bean
    public BaseCorsConfig baseCorsConfig(BaseProperties baseProperties) {
        return new BaseCorsConfig(baseProperties);
    }
}
