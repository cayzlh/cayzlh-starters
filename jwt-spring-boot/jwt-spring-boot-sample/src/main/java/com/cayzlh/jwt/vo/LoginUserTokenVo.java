/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.vo;

import com.cayzlh.framework.jwt.bean.LoginUser;
import com.cayzlh.framework.jwt.shiro.service.LoginToken;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-22.
 */
@Data
@Accessors(chain = true)
public class LoginUserTokenVo implements LoginToken {

    private String token;

    private LoginUser loginUser;
}
