package com.cayzlh.framework.base.advice;

import cn.hutool.json.JSONUtil;
import com.cayzlh.framework.base.annotation.ConvertIgnore;
import com.cayzlh.framework.base.common.BaseResponse;
import com.cayzlh.framework.base.config.ConfigProperties;
import com.cayzlh.framework.base.context.BaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@RestControllerAdvice
@Slf4j
public class BaseResponseAdvice implements ResponseBodyAdvice<Object> {

    @Lazy
    private final ConfigProperties configProperties;

    @Autowired
    public BaseResponseAdvice(
            ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if (configProperties.getConvertable()) {
            ConvertIgnore ignore = methodParameter.getMethodAnnotation(ConvertIgnore.class);
            return ignore == null;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
            Class aClass, ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {
        if (body instanceof BaseResponse) {
            return body;
        } else if (body instanceof String) {
            serverHttpResponse.getHeaders().setContentType(
                    MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
            return JSONUtil.toJsonStr(new BaseResponse<>(body, BaseContextHolder.getRequestId()));
        } else {
            return new BaseResponse<>(body, BaseContextHolder.getRequestId());
        }
    }
}
