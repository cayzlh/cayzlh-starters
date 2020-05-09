/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.config;

import com.cayzlh.framework.base.config.properties.BaseProperties;
import com.cayzlh.framework.util.IniUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Ant丶
 * @date 2020-05-09.
 */
@Slf4j
public class BaseWebMvcConfig implements WebMvcConfigurer {

    private final BaseProperties baseProperties;

    public BaseWebMvcConfig(
            BaseProperties baseProperties) {
        this.baseProperties = baseProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 设置项目静态资源访问
        String resourceHandlers = baseProperties.getResourceHandlers();
        if (StringUtils.isNotBlank(resourceHandlers)) {
            Map<String, String> map = IniUtil.parseIni(resourceHandlers);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String pathPatterns = entry.getKey();
                String resourceLocations = entry.getValue();
                registry.addResourceHandler(pathPatterns)
                        .addResourceLocations(resourceLocations);
            }
        }

        // 设置上传文件访问路径
//        registry.addResourceHandler(springBootPlusProperties.getResourceAccessPatterns())
//                .addResourceLocations("file:" + springBootPlusProperties.getUploadPath());
    }

}
