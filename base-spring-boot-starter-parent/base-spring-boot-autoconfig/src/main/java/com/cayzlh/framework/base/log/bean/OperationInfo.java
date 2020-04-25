/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.log.bean;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ant丶
 * @date 2020-04-25.
 */
@Data
@Accessors(chain = true)
public class OperationInfo implements Serializable {

    private static final long serialVersionUID = 1051984747213348372L;

    /**
     * 是否忽略
     */
    private boolean ignore;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 日志名称
     */
    private String name;

    /**
     * 日志类型
     */
    private Integer type;

    /**
     * 日志备注
     */
    private String remark;

    /**
     * controller类名称
     */
    private String controllerClassName;

    /**
     * controller目标方法名称
     */
    private String controllerMethodName;
}
