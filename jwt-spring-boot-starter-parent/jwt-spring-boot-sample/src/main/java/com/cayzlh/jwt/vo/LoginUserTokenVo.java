/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.vo;

import com.cayzlh.framework.jwt.bo.LoginUserBo;
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

    private LoginUserBo loginUser;
}
