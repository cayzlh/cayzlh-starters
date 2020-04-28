package com.cayzlh.framework.util;

import cn.hutool.json.JSONUtil;
import java.io.PrintWriter;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取当前请求的HttpServletRequest对象
 */
public class HttpServletUtil {

    private HttpServletUtil(){
        throw new AssertionError();
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    public static void printJson(HttpServletResponse response, Object object) throws Exception{
        String UTF8 = "UTF-8";
        response.setCharacterEncoding(UTF8);
        String CONTENT_TYPE = "application/json";
        response.setContentType(CONTENT_TYPE);
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONUtil.toJsonStr(object));
        printWriter.flush();
        printWriter.close();
    }
}
