package com.imooc.ad.index.creativeunit;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware<String,CreativeUnitObject> {
    private static Map<String,CreativeUnitObject> objectMap;
    private static Map<Long, Set<Long>> creativeUnitMap;
    private static Map<Long,Set<Long>> unitCreativeMap;
    static {
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }
    /**
     * 通过K获取索引
     *
     * @param key
     * @return
     */
    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    /**
     * 往索引添加内容
     *
     * @param key
     * @param value
     */
    @Override
    public void add(String key, CreativeUnitObject value) {
//      TODO Test
        objectMap.put(key,value);
        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());
        if (CollectionUtils.isEmpty(unitSet)){
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getAdId(),unitSet);
        }
        unitSet.add(value.getUnitId());
        Set<Long> adSet = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isEmpty(adSet)){
            adSet = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(),adSet);
        }
        adSet.add(value.getAdId());
    }

    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error("CreativeUnitIndex not support update");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        objectMap.remove(key);
        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());
        if (CollectionUtils.isNotEmpty(unitSet)){
            unitSet.remove(value.getUnitId());
        }
        Set<Long> adSet = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isNotEmpty(adSet)){
            adSet.remove(value.getAdId());
        }
    }
}
