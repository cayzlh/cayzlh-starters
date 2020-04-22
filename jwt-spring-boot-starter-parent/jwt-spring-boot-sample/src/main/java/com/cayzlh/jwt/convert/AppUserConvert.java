/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.convert;

import com.cayzlh.framework.jwt.bo.LoginUserBo;
import com.cayzlh.jwt.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Ant丶
 * @date 2020-04-22.
 */
@Mapper
public interface AppUserConvert {

    AppUserConvert INSTANCE = Mappers.getMapper(AppUserConvert.class);

    LoginUserBo appUserToLoginUserBo(AppUser appUser);

}
