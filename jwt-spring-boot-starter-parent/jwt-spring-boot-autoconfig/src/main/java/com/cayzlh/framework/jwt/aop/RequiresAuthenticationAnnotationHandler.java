package com.cayzlh.framework.jwt.aop;

import com.cayzlh.framework.jwt.JwtTokenUtil;
import com.cayzlh.framework.jwt.context.JwtContextHolder;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2020/01/08.
 */
@Aspect
@Component
@Slf4j
public class RequiresAuthenticationAnnotationHandler {

    private static final String TOKEN_PREFIX = "Bear ";

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public RequiresAuthenticationAnnotationHandler(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Pointcut("@annotation(org.apache.shiro.authz.annotation.RequiresAuthentication)")
    private void requiresAuthenticationPoint() {
    }

    @Before("requiresAuthenticationPoint()")
    public void requiresAuthentication() {
        HttpHeaders httpHeaders = generateHttpHeaders();
        String authentication = JwtContextHolder.getAuthentication();
        log.debug("get authentication: {}", authentication);
        if (StringUtils.isBlank(authentication) || !authentication.startsWith(TOKEN_PREFIX)) {
            throw HttpClientErrorException
                    .create(HttpStatus.UNAUTHORIZED, "authentication invalid.", httpHeaders,
                            new byte[]{}, Charset.defaultCharset());
        }
        String token = authentication.substring(5);
        try {
            if (jwtTokenUtil.isTokenExpired(token)) {
                throw HttpClientErrorException
                        .create(HttpStatus.UNAUTHORIZED, "authentication expired.", httpHeaders,
                                new byte[]{}, Charset.defaultCharset());
            }
        } catch (MalformedJwtException | SignatureException e) {
            log.error("requiresAuthentication() catch exception ", e);
            throw HttpClientErrorException
                    .create(HttpStatus.UNAUTHORIZED, e.getMessage(), httpHeaders,
                            new byte[]{}, Charset.defaultCharset());
        }
    }

    private HttpHeaders generateHttpHeaders() {
        HttpServletRequest httpServletRequest = JwtContextHolder.getHttpServletRequest();
        Map<String, List<String>> headerMap = new HashMap<>();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, Lists.newArrayList(httpServletRequest.getHeader(headerName)));
        }
        log.debug("generateHttpHeaders()::{}", headerMap);
        MultiValueMap<String, String> headers = CollectionUtils.toMultiValueMap(headerMap);
        return new HttpHeaders(headers);
    }

}
