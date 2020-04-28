package com.cayzlh.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author chenanyu
 * @date 2019/11/20.
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class JwtSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtSampleApplication.class, args);
    }

}
