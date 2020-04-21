package com.cayzlh.framework.jwt.config;

import lombok.Data;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Data
public class ShiroPermissionProperties {

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
