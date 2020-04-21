package com.cayzlh.framework.jwt.util;

import static com.cayzlh.framework.constant.CommonConstant.JWT_USERNAME;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cayzlh.framework.jwt.config.JwtProperties;
import com.cayzlh.framework.util.UUIDUtil;
import java.time.Duration;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

    private static JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        JwtUtil.jwtProperties = jwtProperties;
        log.info(JSONUtil.toJsonStr(JwtUtil.jwtProperties));
    }

    /**
     * 生成Token
     *
     * @param username 用户名
     * @param salt  盐
     * @param expiredDuration 过期时间和单位
     *
     * @return JWT token
     */
    public static String generateToken(String username, String salt, Duration expiredDuration) {
        if (StringUtils.isBlank(username)) {
            log.error("Failed to generate JWT token, username cannot be empty.");
            // todo 抛出一个异常比较好
            return null;
        }
        log.debug("username: {}", username);

        if (StringUtils.isBlank(salt)) {
            salt = jwtProperties.getSecret();
        }
        log.debug("salt: {}", salt);

        // 过期时间，单位：秒
        Long expireSecond;
        // 默认过期时间为1小时
        if (expiredDuration == null) {
            expireSecond = jwtProperties.getExpiration();
        } else {
            expireSecond = expiredDuration.getSeconds();
        }

        log.debug("expireSecond:{}", expireSecond);
        final Date expireDate = calculateExpirationDate(expireSecond);
        log.debug("expireDate:{}", expireDate);

        // 生成token
        Algorithm algorithm = Algorithm.HMAC256(salt);
        return JWT.create()
                .withClaim(JWT_USERNAME, username)
                // jwt唯一id
                .withJWTId(UUIDUtil.getUuid())
                // 签发人
                .withIssuer(jwtProperties.getIssuer())
                // 主题
                .withSubject(jwtProperties.getSubject())
                // 签发的目标
                .withAudience(jwtProperties.getAudience())
                // 签名时间
                .withIssuedAt(new Date())
                // token过期时间
                .withExpiresAt(expireDate)
                // 签名
                .sign(algorithm);
    }

    /**
     * 校验token
     *
     * @param token token
     * @param salt 盐
     * @return 校验结果
     */
    public static boolean verifyToken(String token, String salt) {
        Algorithm algorithm = Algorithm.HMAC256(salt);
        JWTVerifier verifier = JWT.require(algorithm)
                // 签发人
                .withIssuer(jwtProperties.getIssuer())
                // 主题
                .withSubject(jwtProperties.getSubject())
                // 签发的目标
                .withAudience(jwtProperties.getAudience())
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt != null;
    }

    /**
     * 解析token，获取token数据
     *
     * @param token
     * @return token数据
     */
    public static DecodedJWT getJwtInfo(String token) {
        return JWT.decode(token);
    }

    /**
     * 获取用户名
     *
     * @param token token
     * @return 用户名
     */
    public static String getUsername(String token) {
        if (StringUtils.isBlank(token)){
            // todo 异常
            return null;
        }
        DecodedJWT decodedJwt = getJwtInfo(token);
        return decodedJwt.getClaim(JWT_USERNAME).asString();
    }

    /**
     * 获取创建时间
     *
     * @param token token
     * @return 创建时间
     */
    public static Date getIssuedAt(String token) {
        DecodedJWT decodedJwt = getJwtInfo(token);
        return decodedJwt.getIssuedAt();
    }

    /**
     * 获取过期时间
     *
     * @param token token
     * @return  过期时间
     */
    public static Date getExpireDate(String token) {
        DecodedJWT decodedJwt = getJwtInfo(token);
        return decodedJwt.getExpiresAt();
    }

    /**
     * 判断token是否已过期
     *
     * @param token token
     * @return 是否已过期
     */
    public static boolean isExpired(String token) {
        Date expireDate = getExpireDate(token);
        if (expireDate == null) {
            return true;
        }
        return expireDate.before(new Date());
    }

    private static Date calculateExpirationDate(Long expireSecond) {
        Date now = new Date();
        if (null == expireSecond) {
            return DateUtils.addSeconds(now, jwtProperties.getExpiration().intValue());
        }
        return DateUtils.addSeconds(now, expireSecond.intValue());
    }

}
