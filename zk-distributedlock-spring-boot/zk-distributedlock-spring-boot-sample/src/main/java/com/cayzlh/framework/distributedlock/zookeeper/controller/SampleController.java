package com.cayzlh.framework.distributedlock.zookeeper.controller;

import com.cayzlh.framework.distributedlock.DistributedLock;
import com.cayzlh.framework.distributedlock.zookeeper.annotation.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SampleController {

    private final DistributedLock zookeeperDistributedLock;

    @Autowired
    public SampleController(DistributedLock zookeeperDistributedLock) {
        this.zookeeperDistributedLock = zookeeperDistributedLock;
    }

    @GetMapping("test1")
    @ZkLock
    public String test1() throws InterruptedException {
        Thread.sleep(10000);
        return "test1";
    }

    @GetMapping("lock")
    public String lock() {
        for (int i = 0; i < 5; i++) {
            new RedisLockThread().start();
        }
        return "success.";
    }

    class RedisLockThread extends Thread {

        @Override
        public void run() {
            String key = "lockKey";
            boolean result = zookeeperDistributedLock.lock(key, 10000);
            String threadName = Thread.currentThread().getName();
            log.info("threadName:" + threadName + ( result ? "get lock success : " + key : "get lock failed : " + key));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error("exp", e);
            } finally {
                zookeeperDistributedLock.releaseLock(key);
                log.info("release lock : " + key);
            }
        }
    }
}
