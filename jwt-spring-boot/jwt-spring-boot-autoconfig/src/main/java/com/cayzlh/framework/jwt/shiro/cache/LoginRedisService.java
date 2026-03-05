package com.cayzlh.framework.jwt.shiro.cache;

import com.cayzlh.framework.jwt.bean.LoginUserRedis;
import com.cayzlh.framework.jwt.bean.LoginUser;
import com.cayzlh.framework.jwt.shiro.JwtToken;

/**
 * 登录信息Redis缓存操作服务
 *
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public interface LoginRedisService {

    /**
     * 缓存登录信息
     *
     * @param jwtToken jwtToken
     * @param loginUser loginUserBo
     */
    void cacheLoginInfo(JwtToken jwtToken, LoginUser loginUser);


    /**
     * 刷新登录信息
     *
     * @param oldToken oldToken
     * @param username username
     * @param newJwtToken newJwtToken
     */
    void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken);

    /**
     * 通过用户名，从缓存中获取登录用户LoginSysUserRedisVo
     */
    LoginUserRedis getLoginUserRedisBo(String username);

    /**
     * 获取登录用户对象
     */
    LoginUser getLoginUserBo(String username);

    /**
     * 通过用户名称获取盐值
     */
    String getSalt(String username);

    /**
     * 删除对应用户的Redis缓存
     */
    void deleteLoginInfo(String token, String username);

    /**
     * 判断token在redis中是否存在
     */
    boolean exists(String token);

    /**
     * 删除用户所有登录缓存
     */
    void deleteUserAllCache(String username);

}
