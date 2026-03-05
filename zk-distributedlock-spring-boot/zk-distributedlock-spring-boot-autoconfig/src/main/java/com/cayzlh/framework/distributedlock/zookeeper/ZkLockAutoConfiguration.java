package com.cayzlh.framework.distributedlock.zookeeper;

import com.cayzlh.framework.distributedlock.DistributedLock;
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

    private final ZkConfig zkConfig;

    @Autowired
    public ZkLockAutoConfiguration(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
    }

    @Bean
    public CuratorFramework curatorFramework() {
        log.debug("zookeeper注册中心初始化，服务列表：{}", zkConfig.getConnectString());
        CuratorFramework curatorFramework = getCuratorFramework();
        curatorFramework.start();
        try {
            if (!curatorFramework
                    .blockUntilConnected(zkConfig.getMaxSleepTimeMilliseconds() * zkConfig.getMaxRetries(),
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
                .connectString(zkConfig.getConnectString()).retryPolicy(this.getRetryPolicy())
                .namespace(zkConfig.getNamespace());
        if (zkConfig.getSessionTimeoutMilliseconds() > 0) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeoutMilliseconds());
        }
        if (zkConfig.getConnectionTimeoutMilliseconds() > 0) {
            builder.connectionTimeoutMs(zkConfig.getConnectionTimeoutMilliseconds());
        }
        if (!Strings.isNullOrEmpty(zkConfig.getDigest())) {
            builder.authorization("digest", zkConfig.getDigest().getBytes(Charsets.UTF_8)).aclProvider(
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
                    zkConfig.getBaseSleepTimeMilliseconds(),
                    zkConfig.getMaxRetries(), zkConfig.getMaxSleepTimeMilliseconds());
    }

    @Bean
    @ConditionalOnBean(CuratorFramework.class)
    public DistributedLock zookeeperDistributedLock(CuratorFramework curatorFramework) {
        return new ZookeeperDistributedLock(curatorFramework);
    }
}
