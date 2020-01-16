package com.cayzlh.framework.distributedlock.zookeeper.lock;

public interface DistributedLock {
	
	long TIMEOUT_SECOND = 30;
	
	boolean lock(String key);
	
	boolean lock(String key, long expire);
	
	boolean releaseLock(String key);
}
