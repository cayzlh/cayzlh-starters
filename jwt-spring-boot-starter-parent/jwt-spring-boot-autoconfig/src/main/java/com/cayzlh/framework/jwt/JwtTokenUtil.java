package com.cayzlh.framework.jwt;

import cn.hutool.core.bean.BeanUtil;
import com.cayzlh.framework.jwt.bo.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private final JwtProperties jwtProperties;

    private Clock clock = DefaultClock.INSTANCE;

    @Autowired
    public JwtTokenUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 生成jwt token
     */
    public String generateToken(JwtUser user) {
        Map<String, Object> claims = BeanUtil.beanToMap(user);
        return doGenerateToken(user, claims);
    }

    private String doGenerateToken(JwtUser jwtUser, Map<String, Object> claims) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);
        Key key = getKey(jwtProperties.getSecret());
        // TODO: 2019/12/18
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .setSubject(jwtUser.getUsername())
                .setNotBefore(createdDate)
                .setIssuedAt(createdDate)
                .signWith(key)
                .compact();
    }

    private Key getKey(String secret) {
        if (secret.isEmpty()) {
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        byte[] bytes = secret.getBytes();
        return Keys.hmacShaKeyFor(bytes);
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + jwtProperties.getExpiration());
    }

    /**
     * 根据token中提取Subject
     */
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 获取token的签发时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    /**
     * 获取token的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 通过claimName获取对应的Claim
     */
    public Object getClaimFromToken(String token, String claimName) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get(claimName);
    }

    /**
     * 获取所有的Claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getKey(jwtProperties.getSecret()))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断token是否有效
     */
    public boolean validateToken(String token, JwtUser jwtUser) {
        final String username = getSubjectFromToken(token);
        return username.equals(jwtUser.getUsername()) && !isTokenExpired(token);
    }

    public boolean validateToken(String token, String username) {
        final String subjectFromToken = getSubjectFromToken(token);
        return username.equals(subjectFromToken) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    /**
     * token是否可被刷新
     */
    public boolean isTokenCanRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && !isTokenExpired(
                token);
    }

    private boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        Key key = getKey(jwtProperties.getSecret());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();

    }

    public List<String> getRolesFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (List<String>) claims.get("roles");
    }

    public List<String> getAuthorityFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (List<String>) claims.get("authority");
    }
}
