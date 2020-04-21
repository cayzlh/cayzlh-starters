package com.cayzlh.framework.jwt.shiro.convert;

import com.cayzlh.framework.jwt.bo.LoginUserBo;
import com.cayzlh.framework.jwt.bo.LoginUserRedisBo;
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
    LoginUserRedisBo boToRedisBo(LoginUserBo loginUserBo);

}
