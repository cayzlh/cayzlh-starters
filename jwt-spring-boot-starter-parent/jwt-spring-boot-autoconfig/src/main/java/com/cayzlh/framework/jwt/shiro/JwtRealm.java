package com.cayzlh.framework.jwt.shiro;

import com.cayzlh.framework.jwt.bo.LoginUserRedisBo;
import com.cayzlh.framework.jwt.shiro.cache.LoginRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    private final LoginRedisService loginRedisService;

    public JwtRealm(LoginRedisService loginRedisService) {
        this.loginRedisService = loginRedisService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("doGetAuthorizationInfo principalCollection...");
        // 设置角色/权限信息
        JwtToken jwtToken = (JwtToken) principalCollection.getPrimaryPrincipal();
        // 获取username
        String username = jwtToken.getUsername();
        // 获取登录用户角色权限信息
        LoginUserRedisBo loginUserRedisBo = loginRedisService.getLoginUserRedisBo(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 设置角色
        authorizationInfo.setRoles(SetUtils.hashSet(loginUserRedisBo.getRoleCode()));
        // 设置权限
        authorizationInfo.setStringPermissions(loginUserRedisBo.getPermissionCodes());
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        log.debug("doGetAuthenticationInfo authenticationToken...");
        // 校验token
        JwtToken jwtToken = (JwtToken) authenticationToken;
        if (jwtToken == null) {
            // todo 换成可识别的exception
            throw new AuthenticationException("jwtToken不能为空");
        }
        String salt = jwtToken.getSalt();
        if (StringUtils.isBlank(salt)) {
            // todo 换成可识别的exception
            throw new AuthenticationException("salt不能为空");
        }
        return new SimpleAuthenticationInfo(
                jwtToken,
                salt,
                getName()
        );
    }
}
