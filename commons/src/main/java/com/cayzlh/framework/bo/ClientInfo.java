package com.cayzlh.framework.bo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用户客户端信息对象
 * </p>
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfo implements Serializable {

    private static final long serialVersionUID = -5549531244606897514L;

    /**
     * ip
     */
    private String ip;

    /**
     * ip对应的地址
     */
    private String address;

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 浏览器引擎名称
     */
    private String engineName;

    /**
     * 浏览器引擎版本
     */
    private String engineVersion;

    /**
     * 系统名称
     */
    private String osName;

    /**
     * 平台名称
     */
    private String platformName;

    /**
     * 是否是手机
     */
    private boolean mobile;

    /**
     * 移动端设备名
     */
    private String deviceName;

    /**
     * 移动端设备型号
     */
    private String deviceModel;

}
