package com.cayzlh.framework.jwt.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Data
@ConfigurationProperties(prefix = "cayzlh.framework.shiro")
public class ShiroProperties {

    /**
     * 是否启用shiro, 默认true
     */
    private boolean enable = true;

    /**
     * 路径权限配置
     */
    private String filterChainDefinitions;

    /**
     * 设置无需权限路径集合
     */
    private List<String[]> anon;

    /**
     * 权限配置集合
     */
    private List<ShiroPermissionProperties> permission;

    @Data
    public static class ShiroPermissionProperties {

        /**
         * 路径
         */
        private String url;
        /**
         * 路径数组
         */
        private String[] urls;

        /**
         * 权限
         */
        private String permission;

    }

}
