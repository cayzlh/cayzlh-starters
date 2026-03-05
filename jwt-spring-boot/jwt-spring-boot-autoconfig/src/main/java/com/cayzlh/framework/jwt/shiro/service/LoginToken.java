package com.cayzlh.framework.jwt.shiro.service;

import java.io.Serializable;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public interface LoginToken extends Serializable {

    /**
     * 获取登录token
     *
     * @return 登录token
     */
    String getToken();
}
