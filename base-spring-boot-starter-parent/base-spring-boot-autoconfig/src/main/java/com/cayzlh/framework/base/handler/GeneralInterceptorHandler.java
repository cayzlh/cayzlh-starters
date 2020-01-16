package com.cayzlh.framework.base.handler;

import com.cayzlh.framework.base.context.BaseContextHolder;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@Component
@Slf4j
public class GeneralInterceptorHandler extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("_timestamp", System.currentTimeMillis());

        String requestId = RandomStringUtils.randomNumeric(10);
        MDC.put("requestId", requestId);

        BaseContextHolder.setRequestId(requestId);
        String requestUri = request.getRequestURI();
        log.info("request uri: {}", requestUri);

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        long begin = (long) request.getAttribute("_timestamp");
        long end = System.currentTimeMillis();
        log.info("process success. cost {}ms. ", end - begin);
        MDC.remove("requestId");
        BaseContextHolder.remove();
        super.postHandle(request, response, handler, modelAndView);
    }
}
