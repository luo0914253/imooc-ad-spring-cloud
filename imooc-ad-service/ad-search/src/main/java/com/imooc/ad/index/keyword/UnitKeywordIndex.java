package com.imooc.ad.index.keyword;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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


        log.info("UnitKeywordIndex,after add:{}",unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> value) {

    }

    @Override
    public void delete(String key, Set<Long> value) {

    }
}
