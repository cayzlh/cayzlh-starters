package com.cayzlh.framewrok.base.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class BaseSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseSampleApplication.class, args);
    }

}
