package com.cayzlh.framework.jwt.config;

import static com.cayzlh.framework.constant.CommonConstant.JWT_TOKEN_NAME;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
     * 是否启用鉴权功能
     */
    private boolean enableAuthentication = true;

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
    private Integer refreshTokenCountdown;

    /**
     * shiro相关设置
     */
    private Shiro shiro;

    @Data
    public static class Shiro {

        /**
         * 是否启用shiro, 默认true
         */
        private boolean enable = true;

        /**
         * 路径权限配置
         */
        private String filterChainDefinitions;

        /**
         * 设置无需权限路径集合
         */
        private List<String[]> anon;

        /**
         * 权限配置集合
         */
        @NestedConfigurationProperty
        private List<ShiroPermissionProperties> permission;
    }

}
