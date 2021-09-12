package com.imooc.ad.index.interest;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    private static Map<String,Set<Long>> itUnitMap;
    private static Map<Long,Set<String>> unitItMap;

    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }
    /**
     * 通过K获取索引
     *
     * @param key
     * @return
     */
    @Override
    public Set<Long> get(String key) {
        if (StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }
        Set<Long> unitIds = itUnitMap.get(key);
        if (CollectionUtils.isEmpty(unitIds)){
            return Collections.emptySet();
        }
        return unitIds;
    }

    /**
     * 往索引添加内容
     *
     * @param key
     * @param value
     */
    @Override
    public void add(String key, Set<Long> value) {
        Set<Long> unitIds = CommonUtils.getorCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(value);
        for (Long unit:value){
            Set<String> itSet = CommonUtils.getorCreate(unit, unitItMap, ConcurrentSkipListSet::new);
            itSet.add(key);
        }
//      TODO 调试常规实现
//      倒排索引，根据兴趣获取推广单元Id
//        Set<Long> unitIds = itUnitMap.get(key);
//        if (unitIds != null){
//            unitIds.addAll(value);
//        }else{
//            unitIds = new ConcurrentSkipListSet<>();
//            unitIds.addAll(value);
//            itUnitMap.put(key,unitIds);
//        }
//        for (Long unitId:value){
////          正向索引，根据推广单元获取兴趣
//            Set<String> itSet = unitItMap.get(unitId);
//            if (itSet != null){
//                itSet.add(key);
//            }else {
//                itSet = new ConcurrentSkipListSet<>();
//                itSet.add(key);
//                unitItMap.put(unitId,itSet);
//            }
//        }
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.info("it index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        Set<Long> unitIds = CommonUtils.getorCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(value);
        for (Long unitId:value){
            Set<String> itSet = CommonUtils.getorCreate(unitId, unitItMap, ConcurrentSkipListSet::new);
            itSet.remove(key);
        }
    }
    public boolean match(Long unitId, List<String> itTags){
        if (unitItMap.containsKey(unitId)&& CollectionUtils.isNotEmpty(unitItMap.get(unitId))){
            Set<String> itSet = unitItMap.get(unitId);
            return CollectionUtils.isSubCollection(itTags,itSet);
        }
        return false;
    }
}
