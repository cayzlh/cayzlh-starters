/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ant丶
 * @date 2020-04-25.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogIgnore {

}
