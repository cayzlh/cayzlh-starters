package com.cayzlh.framework.jwt.adapter;

import static com.cayzlh.framework.jwt.constant.HttpHeader.HEADER_AUTHENTICATION;

import com.cayzlh.framework.jwt.context.JwtContextHolder;
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
        String authentication = request.getHeader(HEADER_AUTHENTICATION);
        JwtContextHolder.setAuthentication(authentication);
        JwtContextHolder.setHttpServletRequest(request);
        request.setAttribute("_authentication", authentication);
        log.debug("set authentication[{}] to thread local.", authentication);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        JwtContextHolder.remove();
        String authentication = (String) request.getAttribute("_authentication");
        log.debug("remove authentication[{}] from thread local.", authentication);
        super.postHandle(request, response, handler, modelAndView);
    }

}
