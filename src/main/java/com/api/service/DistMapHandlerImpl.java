package com.api.service;

import com.ds.api.DistMap;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

//TODO, take care of all concurreny needs
public class DistMapHandlerImpl implements DistMapHandler<String, Map<String, ? extends Object>> {

    private LockManager lockManager;
    private DistMap<String, Map<String, ? extends Object>> map;

    public DistMapHandlerImpl(LockManager lockManager, DistMap<String, Map<String, ? extends Object>> map) {
        this.lockManager = lockManager;
        this.map = map;
    }

    private DistMap<String, Map<String, ?>> getMap() {
        return map;
    }

    @Override
    public Map<String, ? extends Object> put(String key, Map<String, ?> stringMap) {

        //System.out.println("Current thread putting start " + Thread.currentThread().getName());
        //get the map for Key, map1
        Map<String, ?> actualMap = getMap().get(key);

        if (actualMap == null) {
            //just add values
            Lock lockFor = lockManager.getLockFor(key);
            Map<String, ?> result = null;
            try {
                lockFor.lock();
                result = getMap().put(key, stringMap);
            } finally {
                lockFor.unlock();
            }
            return result;
        }

        Set<String> incomingStringSet = stringMap.keySet();

        //check if the incoming stringMap has has key that matches any key in map1
        Map<String, ? super Object> innerMap = null;
        for (String item: incomingStringSet) {
            Lock lockFor = lockManager.getLockFor(item);
            //System.out.println("Lock is " + lockFor);
            if (actualMap.keySet().contains(item)) {
                lockFor.lock();
                System.out.println("Locked by " + Thread.currentThread().getName());
                innerMap = (Map<String, ? super Object>)getMap().get(key);
                System.out.println("Thread " + Thread.currentThread().getName() + " got " + innerMap);
                innerMap.put(item, stringMap.get(item));
                //System.out.println("put finished by " + Thread.currentThread().getName());
                lockFor.unlock();
                //System.out.println("Unlocked by " + Thread.currentThread().getName());
            } else {
                System.out.println("No existing key found for " + item);
                lockFor.lock();
                //System.out.println("Locked by " + Thread.currentThread().getName());
                innerMap = (Map<String, ? super Object>) getMap().get(key);
                innerMap.put(item, stringMap.get(item));
                lockFor.unlock();
                //System.out.println("Unlocked by " + Thread.currentThread().getName());
            }

        }

        return innerMap;
    }

    @Override
    public Map<String, ? extends Object> delete(String key) {
        //get lock
        Lock lock = lockManager.getLockFor(key);
        Map<String, ?> result = null;
        try {
            lock.lock();
            //delete key
            result = getMap().delete(key);
        } finally {
            lock.unlock();
            lockManager.deleteLock(key);
        }
        return result;
    }

    @Override
    public Map<String, ? extends Object> get(String key) {
        //do a get on the underlying DS
        return getMap().get(key);
    }

    @Override
    public <K1, V1> Map<K1, V1> createMap() {
        return null;
    }
}
