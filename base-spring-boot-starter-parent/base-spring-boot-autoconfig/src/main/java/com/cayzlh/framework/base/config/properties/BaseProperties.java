/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.config.properties;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@ConfigurationProperties(prefix = "cayzlh.framework.base")
@Data
public class BaseProperties {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 是否将转换成统一的返回格式 如：{"msg":"completed success.","code":0,"data":"test1","requestId":"6892435279"}
     */
    private Boolean convertable = true;

    /**
     * 对lockback的配置
     */
    private Log log;

    /**
     * 跨域配置
     */
    private Cors cors;

    /**
     * 项目静态资源访问配置
     */
    private String resourceHandlers = "/static/**=classpath:/static/\n"
            + "swagger-ui.html=classpath:/META-INF/resources/\n"
            + "/webjars/**=classpath:/META-INF/resources/webjars/\n"
            + "doc.html=classpath:/META-INF/resources/\n"
            + "/favicon.ico=classpath:/META-INF/resources/\n"
            + "/service-worker.js=classpath:/META-INF/resources/\n"
            + "/precache-manifest.**.js=classpath:/META-INF/resources/\n"
            + "/index.html=classpath:/META-INF/resources/\n"
            + "/robots.txt=classpath:/META-INF/resources/\n"
            + "/manifest.json=classpath:/META-INF/resources/\n";

    @Data
    public static class Log {

        /**
         * 是否启用请求日志记录，默认开启
         */
        private boolean enable = true;

        /**
         * 是否启用requestId
         */
        private boolean enableRequestId = true;

        /**
         * 日志目录
         */
        private String path = "logs";

        /**
         * 日志输出级别
         */
        private String level = "INFO";

        /**
         * 请求日志在控制台是否格式化输出，local环境建议开启，服务器环境设置为false
         */
        private boolean requestLogFormat = true;

        /**
         * 响应日志在控制台是否格式化输出，local环境建议开启，服务器环境设置为false
         */
        private boolean responseLogFormat = true;

        /**
         * 排除路径
         */
        private Set<String> excludePaths;

    }

    @Data
    public static class Cors {

        /**
         * 是否启用跨域，默认启用
         */
        private boolean enable = false;

        /**
         * CORS过滤的路径，默认：/**
         */
        private String path = "/**";

        /**
         * 允许访问的源
         */
        private List<String> allowedOrigins = Collections.singletonList(CorsConfiguration.ALL);

        /**
         * 允许访问的请求头
         */
        private List<String> allowedHeaders = Collections.singletonList(CorsConfiguration.ALL);

        /**
         * 是否允许发送cookie
         */
        private boolean allowCredentials = true;

        /**
         * 允许访问的请求方式
         */
        private List<String> allowedMethods = Collections.singletonList(CorsConfiguration.ALL);

        /**
         * 允许响应的头
         */
        private List<String> exposedHeaders = Collections.singletonList("token");

        /**
         * 该响应的有效时间默认为30分钟，在有效时间内，浏览器无须为同一请求再次发起预检请求
         */
        private Long maxAge = 1800L;
    }

}
