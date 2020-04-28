/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.log.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ant丶
 * @date 2020-04-25.
 */
@Data
@Accessors(chain = true)
public class RequestInfo implements Serializable {

    private static final long serialVersionUID = -7602124711009497620L;

    /**
     * 请求路径
     * /api/foobar/add
     */
    private String path;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 请求IP地址
     */
    private String ip;

    /**
     * 请求IP对象
     */
    private String ipAddress;

    /**
     * 请求方式，GET/POST
     */
    private String requestMethod;

    /**
     * 请求内容类型
     */
    private String contentType;

    /**
     * 判断控制器方法参数中是否有RequestBody注解
     */
    private Boolean requestBody;

    /**
     * 请求参数对象
     */
    private Object param;

    /**
     * 请求时间字符串
     */
    private String time;

    /**
     * 请求token
     */
    private String token;

    /**
     * 用户代理字符串
     */
    private String userAgent;

    /**
     * requiresRoles值
     */
    private String requiresRoles;

    /**
     * requiresPermissions值
     */
    private String requiresPermissions;

    /**
     * requiresAuthentication
     */
    private Boolean requiresAuthentication;

    /**
     * requiresUser
     */
    private Boolean requiresUser;

    /**
     * requiresGuest
     */
    private Boolean requiresGuest;

}
