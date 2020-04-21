package com.cayzlh.framework.jwt.adapter;

import com.cayzlh.framework.jwt.JwtContextHolder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2020/01/07.
 */
@Component
@Slf4j
public class JwtInterceptorHandlerAdapter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        JwtContextHolder.setHttpServletRequest(request);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        JwtContextHolder.remove();
        super.postHandle(request, response, handler, modelAndView);
    }

}
