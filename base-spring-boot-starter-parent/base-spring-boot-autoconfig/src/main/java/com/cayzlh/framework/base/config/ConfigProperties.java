package com.cayzlh.framework.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@ConfigurationProperties(prefix = "cayzlh.framework.base")
@Data
@Component
public class ConfigProperties {

    /**
     * 是否将转换成统一的返回格式 如：{"msg":"completed success.","code":0,"data":"test1","requestId":"6892435279"}
     */
    private Boolean convertable = true;

    /**
     * 对lockback的配置
     */
    private Log log;

    @Data
    private static class Log {

        /**
         * 日志目录
         */
        private String path = "logs";

        /**
         * 日志输出级别
         */
        private String level = "INFO";
    }

}
