/*
 * Copyright (c) 2021. https://cayzlh.com All rights reserved.
 */

package com.cayzlh.framework.elasticsearch.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EsProperties.class)
public class EsConfig {

}
