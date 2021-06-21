package com.cayzlh.framework.base.advice;

import cn.hutool.json.JSONUtil;
import com.cayzlh.framework.base.annotation.ConvertIgnore;
import com.cayzlh.framework.base.config.properties.BaseProperties;
import com.cayzlh.framework.base.context.BaseContextHolder;
import com.cayzlh.framework.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@RestControllerAdvice
@Slf4j
@ControllerAdvice
public class BaseResponseAdvice implements ResponseBodyAdvice<Object> {

    @Lazy
    private final BaseProperties baseProperties;

    public BaseResponseAdvice(
            BaseProperties baseProperties) {
        this.baseProperties = baseProperties;
    }

    private static final String[] ignores = new String[]{
            //过滤swagger相关的请求的接口，不然swagger会提示base-url被拦截
            "/swagger-resources",
            "/v2/api-docs"
    };

    private boolean ignoring(String uri) {
        for (String string : ignores) {
            if (uri.contains(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if (baseProperties.getConvertable()) {
            ConvertIgnore ignore = methodParameter.getMethodAnnotation(ConvertIgnore.class);
            return ignore == null;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
            Class aClass, ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {
        //判断url是否需要拦截
        if (this.ignoring(serverHttpRequest.getURI().toString())) {
            return body;
        }

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
