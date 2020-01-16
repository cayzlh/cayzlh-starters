package com.cayzlh.framework.distributedlock.redis.lock;

import com.cayzlh.framework.distributedlock.AbstractDistributedLock;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

@Slf4j
public class RedisDistributedLock extends AbstractDistributedLock {

    private RedisTemplate<Object, Object> redisTemplate;

    private ThreadLocal<String> lockKey = new ThreadLocal<>();

    public static final String UNLOCK_LUA;

    static {
        UNLOCK_LUA = "if redis.call(\"get\",KEYS[1]) == ARGV[1] "
                + "then "
                + "    return redis.call(\"del\",KEYS[1]) "
                + "else "
                + "    return 0 "
                + "end ";
    }

    public RedisDistributedLock(RedisTemplate<Object, Object> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean result = setRedisLock(key, expire);
        /**
         * 如果获取锁失败，进行重试
         */
        while ((!result) && retryTimes-- > 0) {
            try {
                log.info("lock failed, retrying...{}", retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = setRedisLock(key, expire);
        }
        return result;
    }

    private boolean setRedisLock(String key, long expire) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                String uuid = UUID.randomUUID().toString();
                lockKey.set(uuid);
                return connection
                        .set(key.getBytes(), uuid.getBytes(), Expiration.milliseconds(expire),
                                RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("set redis error", e);
        }
        return false;
    }

    @Override
    public boolean releaseLock(String key) {
        /**
         * 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
         * 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
         */
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                String value = lockKey.get();
                return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1, key.getBytes(),
                        value.getBytes());
            };
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("release lock occured an exception", e);
        } finally {
            lockKey.remove();
        }
        return false;
    }

}
