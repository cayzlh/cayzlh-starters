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

    private static final long serialVersionUID = 9212318127410894872L;

    public BusinessException(Integer code, String msg) {
        super(code, msg);
    }
}
