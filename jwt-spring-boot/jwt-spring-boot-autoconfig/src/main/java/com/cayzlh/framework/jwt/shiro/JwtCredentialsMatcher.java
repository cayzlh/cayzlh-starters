package com.cayzlh.framework.jwt.shiro;

import com.cayzlh.framework.jwt.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * JWT证书匹配
 *
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Slf4j
public class JwtCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken,
            AuthenticationInfo authenticationInfo) {
        final String token = authenticationToken.getCredentials().toString();
        final String salt = authenticationInfo.getCredentials().toString();
        return JwtUtil.verifyToken(token, salt);
    }
}
