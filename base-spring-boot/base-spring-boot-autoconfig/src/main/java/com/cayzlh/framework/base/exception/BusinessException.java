package com.cayzlh.framework.base.exception;

import com.cayzlh.framework.exception.CommonException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends CommonException {

    public BusinessException(Integer code, String msg) {
        super(code, msg);
    }
}
