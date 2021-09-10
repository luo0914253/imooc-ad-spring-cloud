package com.imooc.ad.index.keyword;

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
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {

//  倒排索引，keyword到unit的Map
    private static Map<String,Set<Long>> keywordUnitMap;
//  正向索引，推广单元id到关键词的映射
    private static Map<Long,Set<String>> unitKeywordMap;

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }
    /**
     * 通过关键词去获取推广单元支持的关键词
     *
     * @param key
     * @return
     */
    @Override
    public Set<Long> get(String key) {
        if (StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }
        Set<Long> result = keywordUnitMap.get(key);
        if (result == null){
            return Collections.emptySet();
        }
        return result;
    }

    /**
     * 往索引添加内容
     *
     * @param key
     * @param value
     */
    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitKeywordIndex,before add:{}",unitKeywordMap);
        Set<Long> unitIdSet = CommonUtils.getorCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        unitIdSet.addAll(value);
        for (Long unitId:value){
            Set<String> keywordSet = CommonUtils.getorCreate(unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new);
            keywordSet.add(key);
        }
        log.info("UnitKeywordIndex,after add:{}",unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitKeywordIndex,before add:{}",unitKeywordMap);
        Set<Long> unitIds = CommonUtils.getorCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(value);
        for (Long unitId:value){
            Set<String> keywordSet = CommonUtils.getorCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.remove(key);
        }
        log.info("UnitKeywordIndex,after add:{}",unitKeywordMap);
    }
    public boolean match(Long unitId, List<String> keywords){
        if (unitKeywordMap.containsKey(unitId)
                &&CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))){
            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            return CollectionUtils.isSubCollection(keywords,unitKeywords);
        }
        return false;
    }
}
