package com.cayzlh.distributedlock.redis.sample;

import com.cayzlh.framework.distributedlock.redis.annotations.RedisLock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @RedisLock
    @GetMapping("test1")
    public String test1() throws InterruptedException {
        Thread.sleep(10000);
        return "test1";
    }

    @RedisLock(key = "'test121312312312'.concat(#num)")
    @GetMapping("test2")
    public String test2(@RequestParam Integer num) throws InterruptedException {
        Thread.sleep(10000);
        return "test2:"+num;
    }

}
