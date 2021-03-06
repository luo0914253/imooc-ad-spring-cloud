package com.imooc.ad.index.district;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {

    private static Map<String,Set<Long>> districtUnitMap;
    private static Map<Long,Set<String>> unitDistrictMap;

    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }
    /**
     * 通过K获取索引
     *
     * @param key
     * @return
     */
    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    /**
     * 往索引添加内容
     *
     * @param key
     * @param value
     */
    @Override
    public void add(String key, Set<Long> value) {
//      TODO Test
        Set<Long> unitIds = districtUnitMap.get(key);
        if (unitIds != null){
            unitIds.addAll(value);
        }else {
            districtUnitMap.put(key,value);
        }
        for (Long unitId:value){
            Set<String> districtSet = unitDistrictMap.get(unitId);
            if (districtSet != null){
                districtSet.add(key);
            }else {
                districtSet = new ConcurrentSkipListSet<>();
                districtSet.add(key);
                unitDistrictMap.put(unitId,districtSet);
            }
        }
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.info("it index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
//      TODO Test delete
        Set<Long> unitIds = districtUnitMap.get(key);
        if (unitIds != null && CollectionUtils.isSubCollection(value,unitIds)){
            unitIds.removeAll(value);
        }
        for (Long unitId:value){
            Set<String> districtSet = unitDistrictMap.get(unitId);
            if (districtSet != null && districtSet.contains(key)){
                districtSet.remove(key);
            }
        }
    }
    public boolean match(Long adUnitId, List<DistrictFeature.ProvinceAndCity> districts){
//      TODO 操了
        if (unitDistrictMap.containsKey(adUnitId)&&CollectionUtils.isNotEmpty(unitDistrictMap.get(adUnitId))){
            Set<String> unitDistricts = unitDistrictMap.get(adUnitId);
            List<String> targetDistricts = districts.stream().map(d -> CommonUtils.stringConcat(d.getProvince(), d.getCity())).collect(Collectors.toList());
            return CollectionUtils.isSubCollection(targetDistricts,unitDistricts);
        }
        return false;
    }
}
