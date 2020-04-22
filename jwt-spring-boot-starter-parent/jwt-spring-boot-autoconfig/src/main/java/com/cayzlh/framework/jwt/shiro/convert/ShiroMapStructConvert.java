package com.cayzlh.framework.jwt.shiro.convert;

import com.cayzlh.framework.jwt.bo.JwtTokenRedisBo;
import com.cayzlh.framework.jwt.shiro.JwtToken;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 使用mapstruct对象属性复制转换器
 **/
@Mapper
public interface ShiroMapStructConvert {

    ShiroMapStructConvert INSTANCE = Mappers.getMapper(
            ShiroMapStructConvert.class);

    /**
     * JwtToken对象转换成JwtTokenRedisVo
     */
    JwtTokenRedisBo jwtTokenToJwtTokenRedisVo(JwtToken jwtToken);

}
