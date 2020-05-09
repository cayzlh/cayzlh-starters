package com.cayzlh.framework.exception;

import com.cayzlh.framework.constant.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 7669695159869116135L;
    protected Integer errorCode;
    protected String message;

    public CommonException() {
        super();
        this.errorCode = ErrorCode.BASE_ERROR_CODE;
    }

    public CommonException(String message) {
        super(message);
        this.errorCode = ErrorCode.BASE_ERROR_CODE;
        this.message = message;
    }

    public CommonException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

}
