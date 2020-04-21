package com.cayzlh.framework.util;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取当前请求的HttpServletRequest对象
 */
public class HttpServletRequestUtil {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
