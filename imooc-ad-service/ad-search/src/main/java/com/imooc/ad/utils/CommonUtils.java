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
    public static String stringConcat(String...args){
        StringBuilder result = new StringBuilder();
        for (String arg:args){
            result.append(arg);
            result.append("-");
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
}
