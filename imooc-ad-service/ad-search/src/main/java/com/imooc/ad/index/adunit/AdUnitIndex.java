package com.imooc.ad.index.adunit;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long,AdUnitObject> {
    private static Map<Long,AdUnitObject> objectMap;
    static {
        objectMap = new ConcurrentHashMap<>();
    }
//  通过流量类型对所有的索引匹配
    public Set<Long> match(Integer positionType){
//      TODO
        Set<Long> adUnitIds = new HashSet<>();
        objectMap.forEach((k,v)->{
            if (AdUnitObject.isAdSlotTypeOk(positionType,v.getPositionType())){
                adUnitIds.add(k);
            }
        });
        return adUnitIds;
    }
//  通过推广单元的ids获取对应的所以对象
    public List<AdUnitObject> fetch(Collection<Long> adUnitIds){
//      TODO
        if (CollectionUtils.isEmpty(adUnitIds)){
            return Collections.emptyList();
        }
        List<AdUnitObject> result = new ArrayList<>();
        adUnitIds.forEach(u->{
            AdUnitObject object = get(u);
            if (object == null){
                return;
            }
            result.add(object);
        });
        return result;
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
        log.info("before add: {}",objectMap);
        AdUnitObject oldObject = objectMap.get(key);
        if (oldObject == null){
            objectMap.put(key, value);
        }else {
            oldObject.update(value);
        }
        log.info("after add: {}",objectMap);
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        log.info("before add: {}",objectMap);
        objectMap.remove(key);
        log.info("after add: {}",objectMap);
    }
}
