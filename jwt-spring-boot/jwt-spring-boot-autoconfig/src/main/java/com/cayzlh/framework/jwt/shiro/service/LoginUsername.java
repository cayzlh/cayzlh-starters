package com.cayzlh.framework.jwt.shiro.service;

import java.io.Serializable;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public interface LoginUsername extends Serializable {

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    String getUsername();

}
