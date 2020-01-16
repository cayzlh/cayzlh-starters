package com.cayzlh.framework.base.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private Integer code;

    private String msg;

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
