package com.cayzlh.framework.jwt.util;

import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.util.HttpServletUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Slf4j
@Component
public class JwtTokenUtil {

    public static JwtProperties jwtProperties;

    public JwtTokenUtil(JwtProperties jwtProperties) {
        JwtTokenUtil.jwtProperties = jwtProperties;
    }

    /**
     * 获取token名称
     */
    public static String getTokenName() {
        return jwtProperties.getTokenName();
    }

    /**
     * 从请求头或者请求参数中
     */
    public static String getToken() {
        return getToken(HttpServletUtil.getRequest());
    }

    /**
     * 从请求头或者请求参数中
     */
    public static String getToken(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request不能为空");
        }
        // 从请求头中获取token
        String token = request.getHeader(getTokenName());
        if (StringUtils.isBlank(token)) {
            // 从请求参数中获取token
            token = request.getParameter(getTokenName());
        }
        return token;
    }

}
