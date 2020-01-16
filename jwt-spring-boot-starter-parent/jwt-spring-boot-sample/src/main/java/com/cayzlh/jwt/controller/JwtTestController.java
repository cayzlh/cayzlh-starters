package com.cayzlh.jwt.controller;

import com.cayzlh.framework.jwt.annotation.RequiresAuthentication;
import com.cayzlh.framework.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2020/01/08.
 */
@RestController
public class JwtTestController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/test1")
    @RequiresAuthentication
    public String test1() {
        return "test1";
    }

}
