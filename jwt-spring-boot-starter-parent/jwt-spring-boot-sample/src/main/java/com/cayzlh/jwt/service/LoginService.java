/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.service;

import com.cayzlh.jwt.param.LoginParam;
import com.cayzlh.jwt.vo.LoginUserTokenVo;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-22.
 */
public interface LoginService {

    LoginUserTokenVo login(LoginParam loginParam) ;

    void logout(HttpServletRequest request);


}
