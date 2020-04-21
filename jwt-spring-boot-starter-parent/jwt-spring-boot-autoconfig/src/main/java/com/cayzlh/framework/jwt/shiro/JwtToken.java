package com.cayzlh.framework.jwt.shiro;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cayzlh.framework.jwt.util.JwtUtil;
import com.cayzlh.framework.util.IpUtil;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.shiro.authc.HostAuthenticationToken;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken implements HostAuthenticationToken {

    private String host;

    private String username;

    private  String salt;

    private String token;

    private Date createDate;

    private long expireSecond;

    private Date expiredDate;

    /**
     * 主要
     */
    private String principal;

    /**
     * 证书
     */
    private String credentials;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public static JwtToken build(String token, String username, String salt, long expireSecond) {
        DecodedJWT decodedJwt = JwtUtil.getJwtInfo(token);
        Date createDate = decodedJwt.getIssuedAt();
        Date expireDate = decodedJwt.getExpiresAt();
        return JwtToken.builder()
                .username(username).token(token).host(IpUtil.getRequestIp())
                .salt(salt).createDate(createDate).expireSecond(expireSecond).expiredDate(expireDate)
                .build();

    }
}
