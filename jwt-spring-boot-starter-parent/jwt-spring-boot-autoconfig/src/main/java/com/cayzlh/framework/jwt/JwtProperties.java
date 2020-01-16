package com.cayzlh.framework.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cayzlh.framework.jwt")
public class JwtProperties {

    /**
     * 加密密钥(尽量长一点(64位)，Base64之后要达到256长度)
     */
    private String secret = "_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890#";

    /**
     * 签发人
     */
    private String issuer = "";

    /**
     * token超时时间, 单位毫秒, 默认一天
     */
    private Long expiration = 86400000L;

    /**
     * 是否启用鉴权功能
     */
    private boolean enableAuthentication = true;

}
