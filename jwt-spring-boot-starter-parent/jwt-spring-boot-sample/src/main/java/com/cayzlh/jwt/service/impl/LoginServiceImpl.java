/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.service.impl;

import com.cayzlh.framework.base.exception.BusinessException;
import com.cayzlh.framework.jwt.bo.LoginUserBo;
import com.cayzlh.framework.jwt.shiro.JwtToken;
import com.cayzlh.framework.jwt.shiro.cache.LoginRedisService;
import com.cayzlh.framework.jwt.util.JwtUtil;
import com.cayzlh.framework.jwt.util.SaltUtil;
import com.cayzlh.framework.jwt.util.ShiroUtil;
import com.cayzlh.jwt.convert.AppUserConvert;
import com.cayzlh.jwt.entity.AppRole;
import com.cayzlh.jwt.entity.AppUser;
import com.cayzlh.jwt.param.LoginParam;
import com.cayzlh.jwt.service.LoginService;
import com.cayzlh.jwt.vo.LoginUserTokenVo;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-22.
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final LoginRedisService loginRedisService;

    public LoginServiceImpl(
            LoginRedisService loginRedisService) {
        this.loginRedisService = loginRedisService;
    }

    @Override
    public LoginUserTokenVo login(LoginParam loginParam) {
        final String username = loginParam.getUsername();
        AppUser appUser = getAppUserByUsername(username);
        if (null == appUser) {
            throw new BusinessException(1001, "用户名或密码错误");
        }
        // ... 其他登录判断
        LoginUserBo loginUserBo = AppUserConvert.INSTANCE.appUserToLoginUserBo(appUser);

        AppRole role = getRole(username);
        if (null == role) {
            throw new BusinessException(1003, "角色不能为空");
        }

        loginUserBo.setRoleId(role.getId()).setRoleName(role.getRoleName())
                .setRoleCode(role.getRoleCode());

        Set<String> permissionCodes = getPermissionCodes(username);
        if (CollectionUtils.isEmpty(permissionCodes)) {
            throw new BusinessException(1002, "权限列表不能为空");
        }
        loginUserBo.setPermissionCodes(permissionCodes);

        String newSalt = SaltUtil.getSalt(appUser.getSalt());

        String token = JwtUtil.generateToken(username, newSalt);
        log.info("token: {}", token);

        JwtToken jwtToken = JwtToken.build(token,username,newSalt);

        ShiroUtil.login(jwtToken);

        loginRedisService.cacheLoginInfo(jwtToken, loginUserBo);
        log.debug("login success: {}", username);

        LoginUserTokenVo loginUserTokenVo = new LoginUserTokenVo();
        loginUserTokenVo.setToken(token);
        loginUserTokenVo.setLoginUser(loginUserBo);

        return loginUserTokenVo;
    }

    private AppRole getRole(String username) {
        Map<String, AppRole> map = Maps.newHashMap();
        map.put("zhangsan", AppRole.builder().id(1L).roleName("管理员").roleCode("admin").build());
        map.put("lisi", AppRole.builder().id(2L).roleName("游客").roleCode("guest").build());
        return map.get(username);
    }

    private Set<String> getPermissionCodes(String username) {
        Map<String, Set<String>> map = Maps.newHashMap();
        Set<String> permissionCodes1 = Sets.newHashSet("app", "web");
        map.put("zhangsan", permissionCodes1);
        Set<String> permissionCodes2 = Sets.newHashSet("app");
        map.put("lisi", permissionCodes2);
        return map.get(username);

    }

    private AppUser getAppUserByUsername(String username) {
        log.info("调用DAO...");
        Map<String, AppUser> map = Maps.newHashMap();
        map.put("zhangsan",
                AppUser.builder().id(1L).username("zhangsan").nickname("三").salt("111111").build());
        map.put("lisi",
                AppUser.builder().id(2L).username("lisi").nickname("四").salt("222222").build());
        return map.get(username);
    }

    @Override
    public void logout(HttpServletRequest request) {

    }
}
