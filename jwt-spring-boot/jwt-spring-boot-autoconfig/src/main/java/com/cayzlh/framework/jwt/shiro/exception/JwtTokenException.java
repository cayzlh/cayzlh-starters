/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.jwt.shiro.exception;

import com.cayzlh.framework.constant.ErrorCode;
import com.cayzlh.framework.exception.CommonException;

/**
 * @author Ant丶
 * @date 2020-04-22.
 */
public class JwtTokenException extends CommonException {

    public JwtTokenException(String message) {
        super(message);
        this.errorCode = ErrorCode.JWT_TOKEN_ERROR;
        this.message = message;
    }

    public JwtTokenException(Integer errorCode, String message) {
        super(errorCode, message);
    }
}
