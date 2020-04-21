package com.cayzlh.framework.jwt;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public class JwtContextHolder {

    private JwtContextHolder() {
    }

    private static final ThreadLocal<HttpServletRequest> httpServletRequestHolder =
            new ThreadLocal<>();

    public static HttpServletRequest getHttpServletRequest() {
        return httpServletRequestHolder.get();
    }

    public static void setHttpServletRequest(HttpServletRequest request) {
        httpServletRequestHolder.set(request);
    }

    public static void remove() {
        httpServletRequestHolder.remove();
    }
}
