package com.api.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//TODO, Singleton class
public class LockManager {

    private Map<String, Lock> lockMap;

    public LockManager() {
        this.lockMap = new ConcurrentHashMap<>();
    }

    /**
     * gets lock for an existing key.
     * If the lock does not exists, creates the lock, caches it, and then returns it
     * @param key
     * @return
     */
    public Lock getLockFor(String key) {
        if (lockMap.containsKey(key)) {
            return lockMap.get(key);
        }
        Lock lock = new ReentrantLock();
        lockMap.put(key, lock);
        return lockMap.get(key);
    }

    public boolean deleteLock(String key) {
        if (lockMap.containsKey(key)) {
            return lockMap.remove(key) != null;
        }
        return false;
    }
}
