package com.cayzlh.framework.distributedlock.zookeeper.lock;

import com.cayzlh.framework.distributedlock.AbstractDistributedLock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class ZookeeperDistributedLock extends AbstractDistributedLock implements InitializingBean {

	private final static String ROOT_PATH_LOCK = "distributed-lock";

	private CuratorFramework curatorFramework;

	private Map<Long, InterProcessMutex> lockMap = new ConcurrentHashMap<Long, InterProcessMutex>();

	public ZookeeperDistributedLock(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}

	@Override
	public boolean lock(String key, long expire) {
		key = "/"+ ROOT_PATH_LOCK + "/" + key;
		synchronized(key){
			long threadId = Thread.currentThread().getId();
			try {
				// 可重入锁
				InterProcessMutex interProcessMutex;
				if(lockMap.containsKey(threadId)){
					interProcessMutex = lockMap.get(threadId);
				} else{
					interProcessMutex = new InterProcessMutex(curatorFramework, key);
					lockMap.put(threadId, interProcessMutex);
				}
				boolean lock = interProcessMutex.acquire(expire, TimeUnit.SECONDS);
				if(lock){
					log.debug(threadId + " hold lock: [{}].", key);
				}
				return lock;
			} catch (Exception e) {
				lockMap.remove(threadId);
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
		return false;
	}

	@Override
	public boolean releaseLock(String key) {
		key = "/"+ ROOT_PATH_LOCK + "/" + key;
		long threadId = Thread.currentThread().getId();
		try {
			InterProcessMutex interProcessMutex;
			if(lockMap.containsKey(threadId)){
				interProcessMutex = lockMap.get(threadId);
				lockMap.remove(threadId);
			} else{
				interProcessMutex = new InterProcessMutex(curatorFramework, key);
			}
			interProcessMutex.release();
			log.info(threadId + " release lock: [{}] success.", key);
		} catch (Exception e) {
			log.error("释放锁失败", e);
		} finally {
			lockMap.remove(threadId);
		}
		return true;
	}

	// TODO: 使用watcher监控
	@Override
	public void afterPropertiesSet() throws Exception {
		String path = "/" + ROOT_PATH_LOCK;
		try {
			if (curatorFramework.checkExists().forPath(path) == null) {
				curatorFramework.create()
						.creatingParentsIfNeeded()
						.withMode(CreateMode.PERSISTENT)
						.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
						.forPath(path);
			}
			addWatcher(ROOT_PATH_LOCK);
			log.info("root path 的 watcher 事件创建成功");
		} catch (Exception e) {
			log.error("连接到zookeeper服务器失败，请查看日志 >> {}", e.getMessage(), e);
		}
	}

	private void addWatcher(String path) throws Exception {
		String keyPath;
		if (path.equals(ROOT_PATH_LOCK)) {
			keyPath = "/" + path;
		} else {
			keyPath = "/" + ROOT_PATH_LOCK + "/" + path;
		}
		final PathChildrenCache cache = new PathChildrenCache(curatorFramework, keyPath, false);
		cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener((client, event) -> {
			if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
				String oldPath = event.getData().getPath();
				log.info("上一个节点[{}]已经被断开", oldPath);
			}
		});
	}

}

