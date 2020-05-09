/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.base.controller;

import com.cayzlh.framework.base.annotation.ConvertIgnore;
import com.cayzlh.framework.base.config.properties.BaseProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Ant丶
 * @date 2020-05-09.
 */
@Controller
@ApiIgnore
public class IndexController {

    private final BaseProperties baseProperties;

    public IndexController(
            BaseProperties baseProperties) {
        this.baseProperties = baseProperties;
    }

    @GetMapping("/")
    @ResponseBody
    @ConvertIgnore
    public String index() {
        return baseProperties.getProjectName();
    }

    @GetMapping("/docs")
    public String swagger() {
        return "redirect:/swagger-ui.html";
    }
}
