/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.param;

import com.cayzlh.framework.jwt.shiro.service.LoginUsername;
import lombok.Data;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-22.
 */
@Data
public class LoginParam implements LoginUsername {

    private String username;

    private String password;
}
