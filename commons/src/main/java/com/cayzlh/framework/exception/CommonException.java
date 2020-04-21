package com.cayzlh.framework.exception;

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

    private Integer errorCode;
    private String message;

    public CommonException() {
        super();
    }

    public CommonException(String message) {
        super(message);
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
