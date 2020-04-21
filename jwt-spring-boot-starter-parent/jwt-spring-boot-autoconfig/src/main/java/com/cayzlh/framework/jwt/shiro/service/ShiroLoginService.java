package com.cayzlh.framework.jwt.shiro.service;

import com.cayzlh.framework.jwt.shiro.JwtToken;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public interface ShiroLoginService {

    /**
     * 如果(当前时间+倒计时) > 过期时间，则刷新token
     * 并更新缓存
     * 当前token失效，返回新token
     * 前端下次使用新token
     * 如果token继续发往后台，则提示，此token已失效，请使用新token，不在返回新token
     *
     * @param jwtToken token
     * @param httpServletResponse httpServletResponse
     */
    void refreshToken(JwtToken jwtToken, HttpServletResponse httpServletResponse);

}
