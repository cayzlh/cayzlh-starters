package com.cayzlh.framework.jwt.shiro.convert;

import com.cayzlh.framework.jwt.bean.LoginUser;
import com.cayzlh.framework.jwt.bean.LoginUserRedis;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 登录系统用户VO对象属性复制转换器
 **/
@Mapper
public interface LoginUserBoConvert {

    LoginUserBoConvert INSTANCE = Mappers.getMapper(LoginUserBoConvert.class);

    /**
     * LoginSysUserVo对象转换成LoginSysUserRedisVo
     */
    LoginUserRedis boToRedisBo(LoginUser loginUser);

}
