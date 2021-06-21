package com.cayzlh.framework.distributedlock.zookeeper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ZkLock {

    /** 锁的资源，key。支持spring El表达式*/
    String key() default "";

    /**
     * 超时时间，单位秒
     */
    long expire() default 30;

}
