/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.jwt.util;

import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.jwt.config.ShiroProperties;
import com.cayzlh.framework.jwt.shiro.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * @author Ant丶
 * @date 2020-04-22.
 */
@Slf4j
@Component
public class ShiroUtil {
    private static ShiroProperties shiroProperties;

    public ShiroUtil(ShiroProperties shiroProperties) {
        ShiroUtil.shiroProperties = shiroProperties;
    }

    public static void login(JwtToken jwtToken) {
        boolean enableShiro = shiroProperties.isEnable();
        if (enableShiro) {
            Subject subject = SecurityUtils.getSubject();
            subject.login(jwtToken);
        } else {
            log.warn("shiro is not available.");
        }
    }

    public static void logout() {
        boolean enableShiro = shiroProperties.isEnable();
        if (enableShiro) {
            Subject subject = SecurityUtils.getSubject();
            //注销
            subject.logout();
        }
    }
}
