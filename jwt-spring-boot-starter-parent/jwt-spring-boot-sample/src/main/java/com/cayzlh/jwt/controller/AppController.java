/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.controller;

import com.cayzlh.framework.jwt.util.JwtTokenUtil;
import com.cayzlh.jwt.param.LoginParam;
import com.cayzlh.jwt.service.LoginService;
import com.cayzlh.jwt.vo.LoginUserTokenVo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ant丶
 * @date 2020-04-22.
 */
@RestController
@RequestMapping("/app/")
@Slf4j
public class AppController {

    private final LoginService loginService;

    public AppController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("login")
    public LoginUserTokenVo login(@RequestBody LoginParam loginParam, HttpServletResponse response) {
        LoginUserTokenVo loginUserTokenVo = loginService.login(loginParam);
        response.setHeader(JwtTokenUtil.getTokenName(), loginUserTokenVo.getToken());
        return loginUserTokenVo;
    }

    @PostMapping("logout")
    public String logout(HttpServletRequest request) {
        loginService.logout(request);
        return "退出成功";
//        response.setHeader(JwtTokenUtil.getTokenName(), loginUserTokenVo.getToken());
    }

}
