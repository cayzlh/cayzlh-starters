/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.config;

import com.cayzlh.framework.base.config.properties.BaseProperties;
import com.cayzlh.framework.base.config.properties.BaseProperties.Cors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Ant丶
 * @date 2020-05-09.
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "cayzlh.framework.base.cors.enable")
public class BaseCorsConfig {

    private final BaseProperties baseProperties;

    public BaseCorsConfig(BaseProperties baseProperties) {
        this.baseProperties = baseProperties;
    }

    @Bean
    public FilterRegistrationBean<?> corsFilter() {
        Cors corsProperties = baseProperties.getCors();
        log.debug("corsProperties:{}", corsProperties);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 跨域配置
        corsConfiguration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        corsConfiguration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        corsConfiguration.setAllowedMethods(corsProperties.getAllowedMethods());
        corsConfiguration.setAllowCredentials(corsProperties.isAllowCredentials());
        corsConfiguration.setExposedHeaders(corsProperties.getExposedHeaders());
        corsConfiguration.setMaxAge(corsConfiguration.getMaxAge());

        source.registerCorsConfiguration(corsProperties.getPath(), corsConfiguration);
        FilterRegistrationBean<?> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.setEnabled(corsProperties.isEnable());
        return bean;
    }

}
