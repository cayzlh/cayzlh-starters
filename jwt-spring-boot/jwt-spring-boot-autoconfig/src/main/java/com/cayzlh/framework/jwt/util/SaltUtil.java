/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.jwt.util;

import com.cayzlh.framework.jwt.config.JwtProperties;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;

/**
 * @author Ant丶
 * @date 2020-04-22.
 */
@Slf4j
@CommandNaming
public class SaltUtil {

    private static JwtProperties jwtProperties;

    public SaltUtil(JwtProperties jwtProperties) {
        SaltUtil.jwtProperties = jwtProperties;
    }

    /**
     * 生成32位随机盐
     */
    public static String generateSalt() {
        return new SecureRandomNumberGenerator().nextBytes(16).toHex();
    }

    /**
     * 加工盐值
     */
    public static String getSalt(String salt) {
        String newSalt;
        if (jwtProperties.isSaltCheck()) {
            // 包装盐值
            newSalt = DigestUtils.sha256Hex(jwtProperties.getSecret() + salt);
        } else {
            newSalt = DigestUtils.sha256Hex(salt);
        }
        return newSalt;
    }
}
