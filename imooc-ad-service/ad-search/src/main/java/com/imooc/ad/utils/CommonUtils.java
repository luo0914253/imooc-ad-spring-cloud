package com.imooc.ad.utils;

import java.util.Map;
import java.util.function.Supplier;

public class CommonUtils {
    public static <K,V> V getorCreate(K key, Map<K,V> map, Supplier<V> factory){
        return map.computeIfAbsent(key,k -> factory.get());
//      TODO 调试CommonUtils

//        V v = map.get(key);
//        if (v == null){
//            v = factory.get();
//            map.put(key,v);
//        }
//        return v;
    }
}
