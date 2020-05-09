/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.config.properties;

import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
     * 项目静态资源访问配置
     */
    private String resourceHandlers = "/static/**=classpath:/static/\n"
            + "        swagger-ui.html=classpath:/META-INF/resources/\n"
            + "        /webjars/**=classpath:/META-INF/resources/webjars/\n"
            + "        doc.html=classpath:/META-INF/resources/";

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

}
