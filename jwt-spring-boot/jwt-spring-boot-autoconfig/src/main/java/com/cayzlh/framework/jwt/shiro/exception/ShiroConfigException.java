package com.cayzlh.framework.jwt.shiro.exception;

import com.cayzlh.framework.constant.ErrorCode;
import com.cayzlh.framework.exception.CommonException;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public class ShiroConfigException extends CommonException {

    public ShiroConfigException(String message) {
        super(message);
        this.errorCode = ErrorCode.SHIRO_CONFIG_ERROR;
        this.message = message;
    }

    public ShiroConfigException(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
