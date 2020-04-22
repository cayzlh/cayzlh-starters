package com.cayzlh.framework.jwt.bo;

import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * JwtToken Redis缓存对象
 **/
@Data
@Builder
public class JwtTokenRedisBo implements Serializable {
    private static final long serialVersionUID = 1831633309466775223L;
    /**
     * 登录ip
     */
    private String host;
    /**
     * 登录用户名称
     */
    private String username;
    /**
     * 登录盐值
     */
    private String salt;
    /**
     * 登录token
     */
    private String token;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 多长时间过期，默认一小时
     */
    private long expireSecond;
    /**
     * 过期日期
     */
    private Date expireDate;

}
