package com.cayzlh.framework.jwt.context;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2020/01/07.
 */
public class JwtContextHolder {

    private JwtContextHolder() {
    }

    private static final ThreadLocal<String> authenticationTokenHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> httpServletRequestHolder =
            new ThreadLocal<>();

    public static String getAuthentication() {
        return authenticationTokenHolder.get();
    }

    public static HttpServletRequest getHttpServletRequest() {
        return httpServletRequestHolder.get();
    }

    public static void setAuthentication(String token) {
        authenticationTokenHolder.set(token);
    }

    public static void setHttpServletRequest(HttpServletRequest request) {
        httpServletRequestHolder.set(request);
    }

    public static void remove() {
        authenticationTokenHolder.remove();
        httpServletRequestHolder.remove();
    }

}
