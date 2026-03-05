/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.config.properties;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Ant丶
 * @date 2020-05-09.
 */
@Data
@ConfigurationProperties(prefix = "cayzlh.framework.base.swagger")
public class SwaggerProperties {

    /**
     * 是否开启swagger
     */
    private boolean enable = false;

    /**
     * 扫描的基本包
     */
    private String basePackage;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 联系人网址
     */
    private String contactUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 标题
     */
    private String title;

    /**
     * 网址
     */
    private String url;

    /**
     * 版本
     */
    private String version;

    /**
     * 自定义参数配置
     */
    @NestedConfigurationProperty
    private List<ParameterConfig> parameterConfig;

    /**
     * 自定义参数配置
     */
    @Data
    public static class ParameterConfig {

        /**
         * 名称
         */
        private String name;

        /**
         * 描述
         */
        private String description;

        /**
         * 参数类型
         * header, cookie, body, query
         */
        private String type = "head";

        /**
         * 数据类型
         */
        private String dataType = "String";

        /**
         * 是否必填
         */
        private boolean required;

        /**
         * 默认值
         */
        private String defaultValue;

    }
}
