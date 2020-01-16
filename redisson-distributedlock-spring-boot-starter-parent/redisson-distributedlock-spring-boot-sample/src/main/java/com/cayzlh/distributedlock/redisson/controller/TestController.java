package com.cayzlh.distributedlock.redisson.controller;

import com.cayzlh.framework.distributedlock.redisson.RedissonLock;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final RedissonClient redissonClient;

    @Autowired
    public TestController(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @GetMapping("get/{key}")
    public String get(@PathVariable("key") String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        String value = bucket.get();
        long keyCount = redissonClient.getKeys().count();
        return "value:" + value + ", keyCount:" + keyCount;
    }

    @PostMapping("put/{key}/{value}")
    public String put(@PathVariable String key, @PathVariable String value) {
        redissonClient.getBucket(key).set(value, 5, TimeUnit.MINUTES);
        long keyCount = redissonClient.getKeys().count();
        return "keyCount:" + keyCount;
    }

    @RedissonLock
    @GetMapping("test1")
    public String test1() throws InterruptedException {
        Thread.sleep(10000);
        return "test1";
    }

    @RedissonLock(key = "'test121312312312'.concat(#num)")
    @GetMapping("test2")
    public String test2(@RequestParam Integer num) throws InterruptedException {
        Thread.sleep(10000);
        return "test2:"+num;
    }

}
