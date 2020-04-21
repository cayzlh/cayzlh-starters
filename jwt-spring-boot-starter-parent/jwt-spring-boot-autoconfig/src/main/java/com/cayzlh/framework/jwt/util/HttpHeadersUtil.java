package com.cayzlh.framework.jwt.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Slf4j
public class HttpHeadersUtil {

    public static HttpHeaders generateHttpHeaders() {
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
