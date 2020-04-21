package com.cayzlh.framework.constant;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public interface CommonConstant {

    String JWT_USERNAME = "username";

    String JWT_TOKEN_NAME = "token";

    /**
     * 用户浏览器代理
     */
    String USER_AGENT = "User-Agent";

    /**
     * JWT刷新新token响应状态码
     */
    int JWT_REFRESH_TOKEN_CODE = 460;

    /**
     * JWT刷新新token响应状态码，
     * Redis中不存在，但jwt未过期，不生成新的token
     */
    int JWT_INVALID_TOKEN_CODE = 461;

}
