package com.imooc.ad.index.adplan;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long,AdUnitObject> {
    private static Map<Long,AdUnitObject> objectMap;
    static {
        objectMap = new ConcurrentHashMap<>();
    }
    @Override
    public AdUnitObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        log.info("before add: {}",objectMap);
        objectMap.put(key, value);
        log.info("after add: {}",objectMap);
    }

    @Override
    public void update(Long key, AdUnitObject value) {

    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        log.info("before add: {}",objectMap);
        objectMap.remove(key);
        log.info("after add: {}",objectMap);
    }
}
