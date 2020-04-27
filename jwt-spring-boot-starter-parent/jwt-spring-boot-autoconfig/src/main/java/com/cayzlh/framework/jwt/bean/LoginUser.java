/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */
package com.cayzlh.framework.jwt.bean;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Data
@Accessors(chain = true)
public class LoginUser implements Serializable {

    private Long id;

    private String username;

    private String nickname;

    private Integer gender;

    private Integer state;

    private Long departmentId;

    private String departmentName;

    private Long roleId;

    private String roleName;

    private String roleCode;

    private Set<String> permissionCodes;

}
