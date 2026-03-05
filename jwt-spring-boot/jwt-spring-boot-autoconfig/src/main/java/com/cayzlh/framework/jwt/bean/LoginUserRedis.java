/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.jwt.bean;

import com.cayzlh.framework.bo.ClientInfoBo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录用户Redis对象，后台使用
 **/
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class LoginUserRedis extends LoginUser {

    private static final long serialVersionUID = -3858850188055605806L;

    /**
     * 包装后的盐值
     */
    private String salt;

    /**
     * 登录ip
     */
    private ClientInfoBo clientInfoBo;

}
