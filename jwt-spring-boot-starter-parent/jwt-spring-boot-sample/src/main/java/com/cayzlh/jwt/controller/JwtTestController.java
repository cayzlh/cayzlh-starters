package com.cayzlh.jwt.controller;

import com.cayzlh.framework.jwt.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2020/01/08.
 */
@RestController
public class JwtTestController {

    @GetMapping("/test1")
    @RequiresAuthentication
    public String test1() {
        return "test1";
    }

}
