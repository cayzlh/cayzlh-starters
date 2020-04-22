package com.cayzlh.framework.jwt.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
    @NestedConfigurationProperty
    private List<ShiroPermissionProperties> permission;

}
