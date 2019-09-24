package com.api.service;

import java.util.Map;

public interface DistMapHandler<K, V> {

    V put(K key, V v);
    V delete(K key);
    V get(K key);
    <K1, V1> Map<K1, V1> createMap();
}
