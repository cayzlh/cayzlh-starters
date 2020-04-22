package com.cayzlh.framework.jwt.shiro.cache.impl;

import cn.hutool.json.JSONUtil;
import com.cayzlh.framework.bo.ClientInfoBo;
import com.cayzlh.framework.constant.CommonRedisKey;
import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.jwt.bo.JwtTokenRedisBo;
import com.cayzlh.framework.jwt.bo.LoginUserBo;
import com.cayzlh.framework.jwt.bo.LoginUserRedisBo;
import com.cayzlh.framework.jwt.shiro.JwtToken;
import com.cayzlh.framework.jwt.shiro.cache.LoginRedisService;
import com.cayzlh.framework.jwt.shiro.convert.LoginUserBoConvert;
import com.cayzlh.framework.jwt.shiro.convert.ShiroMapStructConvert;
import com.cayzlh.framework.util.ClientInfoUtil;
import com.cayzlh.framework.util.HttpServletUtil;
import com.cayzlh.framework.util.RedisUtil;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Slf4j
@Service
public class LoginRedisServiceImpl implements LoginRedisService {

    private final JwtProperties jwtProperties;

    private final RedisUtil redisUtil;

    public LoginRedisServiceImpl(JwtProperties jwtProperties,
            RedisUtil redisUtil) {
        this.jwtProperties = jwtProperties;
        this.redisUtil = redisUtil;
    }

    @Override
    public void cacheLoginInfo(JwtToken jwtToken, LoginUserBo loginUserBo) {
        if (jwtToken == null) {
            throw new IllegalArgumentException("jwtToken can not be null!");
        }
        if (loginUserBo == null) {
            throw new IllegalArgumentException("loginSysUserVo can not be null!");
        }
        // token
        String token = jwtToken.getToken();
        // 盐值
        String salt = jwtToken.getSalt();
        // 登录用户名称
        String username = loginUserBo.getUsername();
        // token md5值
        String tokenMd5 = DigestUtils.md5Hex(token);

        // Redis缓存JWT Token信息
        JwtTokenRedisBo jwtTokenRedisVo = ShiroMapStructConvert.INSTANCE
                .jwtTokenToJwtTokenRedisVo(jwtToken);

        // 用户客户端信息
        ClientInfoBo clientInfoBo = ClientInfoUtil.get(HttpServletUtil.getRequest());

        // Redis缓存登录用户信息
        // 将LoginSysUserVo对象复制到LoginSysUserRedisVo，使用mapstruct进行对象属性复制
        LoginUserRedisBo loginUserRedisBo = LoginUserBoConvert.INSTANCE.boToRedisBo(loginUserBo);
        loginUserRedisBo.setSalt(salt);
        loginUserRedisBo.setClientInfoBo(clientInfoBo);

        // Redis过期时间与JwtToken过期时间一致
        Duration expireDuration = Duration.ofSeconds(jwtToken.getExpireSecond());

        // 判断是否启用单个用户登录，如果是，这每个用户只有一个有效token
        boolean singleLogin = jwtProperties.isSingleLogin();
        if (singleLogin) {
            deleteUserAllCache(username);
        }

        // 1. tokenMd5:jwtTokenRedisVo
        String loginTokenRedisKey = String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5);
        redisUtil.setEx(loginTokenRedisKey, JSONUtil.toJsonStr(jwtTokenRedisVo),
                expireDuration.getSeconds(), TimeUnit.SECONDS);
        // 2. username:loginSysUserRedisVo
        redisUtil.setEx(String.format(CommonRedisKey.LOGIN_USER, username),
                JSONUtil.toJsonStr(loginUserRedisBo),
                expireDuration.getSeconds(), TimeUnit.SECONDS);
        // 3. salt hash,方便获取盐值鉴权
        redisUtil.setEx(String.format(CommonRedisKey.LOGIN_SALT, username), salt,
                expireDuration.getSeconds(), TimeUnit.SECONDS);
        // 4. login user token
        redisUtil.setEx(String.format(CommonRedisKey.LOGIN_USER_TOKEN, username, tokenMd5),
                loginTokenRedisKey, expireDuration.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken) {
        // 获取缓存的登录用户信息
        LoginUserRedisBo loginUserRedisBo = getLoginUserRedisBo(username);
        // 删除之前的token信息
        deleteLoginInfo(oldToken, username);
        // 缓存登录信息
        cacheLoginInfo(newJwtToken, loginUserRedisBo);
    }

    @Override
    public LoginUserRedisBo getLoginUserRedisBo(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        return JSONUtil.toBean(redisUtil.get(String.format(CommonRedisKey.LOGIN_USER, username)),
                LoginUserRedisBo.class);
    }

    @Override
    public LoginUserBo getLoginUserBo(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        return getLoginUserRedisBo(username);
    }

    @Override
    public String getSalt(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("username不能为空");
        }
        return redisUtil.get(String.format(CommonRedisKey.LOGIN_SALT, username));
    }

    @Override
    public void deleteLoginInfo(String token, String username) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        if (username == null) {
            throw new IllegalArgumentException("username不能为空");
        }
        String tokenMd5 = DigestUtils.md5Hex(token);
        // 1. delete tokenMd5
        redisUtil.delete(String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisUtil.delete(String.format(CommonRedisKey.LOGIN_USER, username));
        // 3. delete salt
        redisUtil.delete(String.format(CommonRedisKey.LOGIN_SALT, username));
        // 4. delete user token
        redisUtil.delete(String.format(CommonRedisKey.LOGIN_USER_TOKEN, username, tokenMd5));
    }

    @Override
    public boolean exists(String token) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        String tokenMd5 = DigestUtils.md5Hex(token);
        return redisUtil.hasKey(String.format(CommonRedisKey.LOGIN_TOKEN, tokenMd5));
    }

    @Override
    public void deleteUserAllCache(String username) {
        Set<String> userTokenMd5Set = redisUtil.keys(String.format(CommonRedisKey.LOGIN_USER_ALL_TOKEN, username));
        if (CollectionUtils.isEmpty(userTokenMd5Set)) {
            return;
        }

        // 1. 删除登录用户的所有token信息
        List<String> tokenMd5List = redisUtil.multiGet(userTokenMd5Set);
        redisUtil.delete(tokenMd5List);
        // 2. 删除登录用户的所有user:token信息
        redisUtil.delete(userTokenMd5Set);
        // 3. 删除登录用户信息
        redisUtil.delete(String.format(CommonRedisKey.LOGIN_USER, username));
        // 4. 删除登录用户盐值信息
        redisUtil.delete(String.format(CommonRedisKey.LOGIN_SALT, username));
    }
}
