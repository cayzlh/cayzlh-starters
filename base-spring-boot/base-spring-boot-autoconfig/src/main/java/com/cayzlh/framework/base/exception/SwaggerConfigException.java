package com.cayzlh.framework.base.exception;

import com.cayzlh.framework.constant.ErrorCode;
import com.cayzlh.framework.exception.CommonException;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 */
public class SwaggerConfigException extends CommonException {

    private static final long serialVersionUID = 1660839511808387846L;

    public SwaggerConfigException(String message) {
        super(message);
        this.errorCode = ErrorCode.SWAGGER_ERROR;
        this.message = message;
    }

    public SwaggerConfigException(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
