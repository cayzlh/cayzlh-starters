package com.cayzlh.framework.base.context;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
public class BaseContextHolder {

    private BaseContextHolder() {
    }

    private static final ThreadLocal<String> REQUEST_ID_HOLDER = new ThreadLocal<>();

    public static String getRequestId() {
        return REQUEST_ID_HOLDER.get();
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID_HOLDER.set(requestId);
    }

    public static void remove() {
        REQUEST_ID_HOLDER.remove();
    }

}
