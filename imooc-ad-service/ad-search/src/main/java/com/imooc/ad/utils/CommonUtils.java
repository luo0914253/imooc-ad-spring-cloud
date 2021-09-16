package com.imooc.ad.utils;

import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
    public static Date parseStringDate(String dateString){
        try {
            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.US);
            return DateUtils.addHours(dateFormat.parse(dateString),-8);
        }catch (ParseException e){
            return null;
        }
    }
}
