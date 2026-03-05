/*
 * Copyright (c) 2021. https://cayzlh.com All rights reserved.
 */

package com.cayzlh.framework.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cayzlh.framework.es")
@Data
public class EsProperties {

    private String host = "http://127.0.0.1";

    private int port = 9200;





}
