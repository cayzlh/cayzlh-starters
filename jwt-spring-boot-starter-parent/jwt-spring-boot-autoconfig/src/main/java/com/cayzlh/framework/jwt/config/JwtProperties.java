package com.cayzlh.framework.jwt.config;

import static com.cayzlh.framework.constant.CommonsConstant.JWT_TOKEN_NAME;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cayzlh.framework.jwt")
public class JwtProperties {

    /**
     * 加密密钥
     */
    private String secret = "999999";

    /**
     * 签发人
     */
    private String issuer = "";

    /**
     * 主题
     */
    private String subject = "";

    /**
     * 签发目标
     */
    private String audience = "";

    /**
     * token超时时间, 单位 秒, 默认1小时
     */
    private Long expiration = 3600L;

    /**
     * token名称，默认：token
     */
    private String tokenName = JWT_TOKEN_NAME;

    /**
     * redis校验jwt token是否存在
     */
    private boolean redisCheck = false;

    /**
     * 单用户登录，一个用户只能又一个有效的token
     */
    private boolean singleLogin = false;

    /**
     * 是否进行盐值校验
     */
    private boolean saltCheck = false;

    /**
     * 是否刷新token，默认为true
     */
    private boolean refreshToken = true;

    /**
     * 刷新token倒计时，默认10分钟，10*60=600
     */
    private Integer refreshTokenCountdown = 600;

}
