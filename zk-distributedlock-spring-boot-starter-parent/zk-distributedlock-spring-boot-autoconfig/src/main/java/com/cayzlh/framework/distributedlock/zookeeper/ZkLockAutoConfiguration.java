package com.cayzlh.framework.distributedlock.zookeeper;

import com.cayzlh.framework.distributedlock.zookeeper.lock.DistributedLock;
import com.cayzlh.framework.distributedlock.zookeeper.lock.ZookeeperDistributedLock;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ZkConfig.class)
@Slf4j
public class ZkLockAutoConfiguration {

    private final ZkConfig zk;

    @Autowired
    public ZkLockAutoConfiguration(ZkConfig zk) {
        this.zk = zk;
    }

    @Bean
    public CuratorFramework curatorFramework() {
        log.debug("zookeeper注册中心初始化，服务列表：{}", zk.getConnectString());
        CuratorFramework curatorFramework = getCuratorFramework();
        curatorFramework.start();
        try {
            if (!curatorFramework
                    .blockUntilConnected(zk.getMaxSleepTimeMilliseconds() * zk.getMaxRetries(),
                            TimeUnit.MILLISECONDS)) {
                curatorFramework.close();
                throw new KeeperException.OperationTimeoutException();
            }
        } catch (Exception e) {
            log.error("zk exception", e);
        }
        return curatorFramework;
    }

    private CuratorFramework getCuratorFramework() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zk.getConnectString()).retryPolicy(this.getRetryPolicy())
                .namespace(zk.getNamespace());
        if (zk.getSessionTimeoutMilliseconds() > 0) {
            builder.sessionTimeoutMs(zk.getSessionTimeoutMilliseconds());
        }
        if (zk.getConnectionTimeoutMilliseconds() > 0) {
            builder.connectionTimeoutMs(zk.getConnectionTimeoutMilliseconds());
        }
        if (!Strings.isNullOrEmpty(zk.getDigest())) {
            builder.authorization("digest", zk.getDigest().getBytes(Charsets.UTF_8)).aclProvider(
                    new ACLProvider() {
                        @Override
                        public List<ACL> getDefaultAcl() {
                            return Ids.CREATOR_ALL_ACL;
                        }

                        @Override
                        public List<ACL> getAclForPath(String s) {
                            return Ids.CREATOR_ALL_ACL;
                        }
                    });
        }
        return builder.build();
    }

    private RetryPolicy getRetryPolicy() {
        return new ExponentialBackoffRetry(
                    zk.getBaseSleepTimeMilliseconds(),
                    zk.getMaxRetries(), zk.getMaxSleepTimeMilliseconds());
    }

    @Bean
    @ConditionalOnBean(CuratorFramework.class)
    public DistributedLock zookeeperDistributedLock(CuratorFramework curatorFramework) {
        return new ZookeeperDistributedLock(curatorFramework);
    }
}
