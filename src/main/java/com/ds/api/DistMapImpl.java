package com.ds.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO,  this is a singleton class
public class DistMapImpl implements DistMap<String, Map<String, ? extends Object>> {

    //TODO, better initialization logic
    private Map<String, Map<String, ? extends Object>> map = new ConcurrentHashMap<>();

    @Override
    public Map<String, ?> put(String key, Map<String, ? extends Object> value) {
        return map.put(key, value);
    }

    @Override
    public Map<String, ? extends Object> delete(String key) {
        return map.remove(key);
    }

    @Override
    public Map<String, ? extends Object> get(String key) {
        return map.get(key);
    }

    @Override
    public String toString() {
        return "DistMapImpl{" +
                "map=" + map +
                '}';
    }
}
