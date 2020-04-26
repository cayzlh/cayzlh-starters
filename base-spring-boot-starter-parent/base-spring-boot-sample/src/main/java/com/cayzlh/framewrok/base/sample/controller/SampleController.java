package com.cayzlh.framewrok.base.sample.controller;

import com.cayzlh.framework.base.annotation.ConvertIgnore;
import com.cayzlh.framework.base.exception.BusinessException;
import com.cayzlh.framewrok.base.sample.TestVo;
import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@RestController
public class SampleController {

    @GetMapping("/test1")
    public String test1() {
        return "test1";
    }

    @GetMapping("/test2")
    public Map test2() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "test1");
        return map;
    }

    @GetMapping("/test3")
    public TestVo test3() {
        return TestVo.builder().id(1).name("test3").build();
    }

    @GetMapping("/test4")
    @ConvertIgnore
    public TestVo test4() {
        return TestVo.builder().id(2).name("test4").build();
    }

    @GetMapping("/test5")
    public String test5() throws Exception {
        throw new Exception("12344");
    }

    @GetMapping("/test6")
    public String test6() {
        throw new BusinessException(123, "1234");
    }

    @PostMapping("/test7")
    public TestVo test7(@RequestBody @Validated TestVo vo, BindingResult bindingResult) {
        return vo;
    }

}
