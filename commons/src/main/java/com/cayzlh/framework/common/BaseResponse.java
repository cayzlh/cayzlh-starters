/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.common;

import com.cayzlh.framework.constant.ResponseCode;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/19.
 */
@Data
@Builder
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {

    private String requestId;

    private Integer code;

    private String msg;

    private T data;

    public BaseResponse() {
        this.code = ResponseCode.SUCCESS;
        this.msg = "completed success.";
    }

    public BaseResponse(T data) {
        this.code = ResponseCode.SUCCESS;
        this.msg = "completed success.";
        this.data = data;
    }

    public BaseResponse(T data, String requestId) {
        this.code = ResponseCode.SUCCESS;
        this.msg = "completed success.";
        this.requestId = requestId;
        this.data = data;
    }
}
