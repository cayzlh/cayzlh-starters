package com.cayzlh.framework.jwt.shiro;

import com.cayzlh.framework.common.BaseResponse;
import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.jwt.shiro.cache.LoginRedisService;
import com.cayzlh.framework.jwt.shiro.service.ShiroLoginService;
import com.cayzlh.framework.jwt.util.JwtTokenUtil;
import com.cayzlh.framework.jwt.util.JwtUtil;
import com.cayzlh.framework.util.HttpServletUtil;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Slf4j
public class JwtFilter extends AuthenticatingFilter {

    private final ShiroLoginService shiroLoginService;

    private final JwtProperties jwtProperties;

    private final LoginRedisService loginRedisService;

    public JwtFilter(ShiroLoginService shiroLoginService,
            JwtProperties jwtProperties,
            LoginRedisService loginRedisService) {
        this.shiroLoginService = shiroLoginService;
        this.jwtProperties = jwtProperties;
        this.loginRedisService = loginRedisService;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest,
            ServletResponse servletResponse) {
        String token = JwtTokenUtil.getToken();
        if (StringUtils.isBlank(token)) {
            log.error("failed to build jwtToken, the token string can not be empty.");
            return null;
        }
        if (JwtUtil.isExpired(token)) {
            log.error("failed to build jwtToken, the token is expired.");
            return null;
        }

        // 如果开启redis二次校验，或者设置为单个用户token登录，则先在redis中判断token是否存在
        if (jwtProperties.isRedisCheck() || jwtProperties.isSingleLogin()) {
            boolean redisExpired = loginRedisService.exists(token);
            if (!redisExpired) {
                log.error("the token does not exist in redis.");
                return null;
            }
        }

        String username = JwtUtil.getUsername(token);
        String salt;
        if (jwtProperties.isSaltCheck()) {
            salt = loginRedisService.getSalt(username);
        } else {
            salt = jwtProperties.getSecret();
        }
        return JwtToken.build(token, username, salt, jwtProperties.getExpiration());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)
            throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(servletResponse);
        // 返回401
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 设置响应码为401或者直接输出消息
        String url = httpServletRequest.getRequestURI();
        log.error("access error. url：{}", url);
        BaseResponse<?> response = getAccessDeniedResponse(url);
        HttpServletUtil.printJson(httpServletResponse, response);
        return false;
    }

    private BaseResponse<?> getAccessDeniedResponse(String url) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setMsg(
                "authentication invalid, please try to log in again to get the correct token.");
        response.setData("onAccessDenied url: " + url);
        return response;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
            Object mappedValue) {
        String url = WebUtils.toHttp(request).getRequestURI();
        log.debug("isAccessAllowed url:{}", url);
        if (this.isLoginRequest(request, response)) {
            return true;
        }
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (Exception e) {
            log.error("access error.", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken authenticationToken, Subject subject,
            ServletRequest request, ServletResponse response) {
        String url = WebUtils.toHttp(request).getRequestURI();
        log.debug("鉴权成功,token:{},url:{}", authenticationToken, url);
        // 刷新token
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String newToken = shiroLoginService.refreshToken(jwtToken);
        HttpServletResponse httpServletResponse = HttpServletUtil.getResponse();
        httpServletResponse.setHeader(JwtTokenUtil.getTokenName(), newToken);
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
            ServletRequest request, ServletResponse response) {
        log.error("登录失败，token: " + token + ",error: " + e.getMessage(), e);
        return false;
    }
}
