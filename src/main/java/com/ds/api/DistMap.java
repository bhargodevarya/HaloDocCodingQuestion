package com.ds.api;

import java.util.Map;

public interface DistMap<K, V> {

    V put(K key, V value);
    V delete(K key);
    V get(K key);
}
