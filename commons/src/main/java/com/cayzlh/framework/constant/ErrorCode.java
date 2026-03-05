/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.constant;

/**
 * @author Ant丶
 * @date 2020-04-22.
 */
public interface ErrorCode {

    /**
     * 框架自定义异常600开头
     */
    int BASE_ERROR_CODE = 600;

    /**
     * shiro配置异常
     */
    int SHIRO_CONFIG_ERROR = BASE_ERROR_CODE + 1;

    /**
     * Jwt token异常
     */
    int JWT_TOKEN_ERROR = BASE_ERROR_CODE + 2;

    /**
     * swagger 配置异常
     */
    int SWAGGER_ERROR = BASE_ERROR_CODE + 3;

}
