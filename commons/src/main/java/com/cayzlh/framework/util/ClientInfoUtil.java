package com.cayzlh.framework.util;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.cayzlh.framework.bo.ClientInfoBo;
import com.cayzlh.framework.bo.DeviceInfoBo;
import com.cayzlh.framework.constant.CommonsConstant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public class ClientInfoUtil {
    private static final Pattern DEVICE_INFO_PATTERN = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?Build/(\\S*?)[;)]");
    private static final Pattern DEVICE_INFO_PATTERN_1 = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?\\)");

    /**
     * 获取用户客户端信息
     */
    public static ClientInfoBo get(HttpServletRequest request){
        String userAgent = request.getHeader(CommonsConstant.USER_AGENT);
        return get(userAgent);
    }

    /**
     * 获取用户客户端信息
     */
    public static ClientInfoBo get(String userAgentString){
        ClientInfoBo clientInfoBo = new ClientInfoBo();

        UserAgent userAgent = UserAgentUtil.parse(userAgentString);

        // 浏览器名称
        clientInfoBo.setBrowserName(userAgent.getBrowser().getName());
        // 浏览器版本
        clientInfoBo.setBrowserVersion(userAgent.getVersion());
        // 浏览器引擎名称
        clientInfoBo.setEngineName(userAgent.getEngine().getName());
        // 浏览器引擎版本
        clientInfoBo.setEngineVersion(userAgent.getEngineVersion());
        // 用户操作系统名称
        clientInfoBo.setOsName(userAgent.getOs().getName());
        // 用户操作平台名称
        clientInfoBo.setPlatformName(userAgent.getPlatform().getName());
        // 是否是手机
        clientInfoBo.setMobile(userAgent.isMobile());

        // 获取移动设备名称和机型
        DeviceInfoBo deviceInfoBo = getDeviceInfo(userAgentString);
        // 设置移动设备名称和机型
        clientInfoBo.setDeviceName(deviceInfoBo.getName());
        clientInfoBo.setDeviceModel(deviceInfoBo.getModel());

        // ip
        clientInfoBo.setIp(IpUtil.getRequestIp());

        return clientInfoBo;
    }

    /**
     * 获取移动端用户设备的名称和机型
     */
    public static DeviceInfoBo getDeviceInfo(String userAgentString){
        DeviceInfoBo deviceInfoBo = new DeviceInfoBo();
        try {

            Matcher matcher = DEVICE_INFO_PATTERN.matcher(userAgentString);
            String model = null;
            String name = null;

            if (matcher.find()) {
                model = matcher.group(1);
                name = matcher.group(2);
            }

            if (model == null && name == null){
                matcher = DEVICE_INFO_PATTERN_1.matcher(userAgentString);
                if (matcher.find()) {
                    model = matcher.group(1);
                }
            }

            deviceInfoBo.setName(name);
            deviceInfoBo.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceInfoBo;
    }
}
