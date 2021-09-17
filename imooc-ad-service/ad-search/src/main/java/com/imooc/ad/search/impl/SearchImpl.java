package com.imooc.ad.search.impl;

import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.search.ISearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.search.vo.feature.FeatureRelation;
import com.imooc.ad.search.vo.feature.ItFeature;
import com.imooc.ad.search.vo.feature.KeywordFeature;
import com.imooc.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SearchImpl implements ISearch {
    @Override
    public SearchResponse fetchAds(SearchRequest request) {
//      TODO 越写越懵逼
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        FeatureRelation relation = request.getFeatureInfo().getRelation();
        SearchResponse response = new SearchResponse();
//      构造响应对象
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();
        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;
//          根据流量类型获取初始AdUnit
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());
            if (relation == FeatureRelation.AND){
                filterKeywordFeature(adUnitIdSet,keywordFeature);
                filterDistrictFeature(adUnitIdSet,districtFeature);
                filterItTagFeature(adUnitIdSet,itFeature);
                targetUnitIdSet = adUnitIdSet;
            }else {
                targetUnitIdSet = getORRelationUnitIds(adUnitIdSet,keywordFeature,districtFeature,itFeature);
            }
        }
        return null;
    }
    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,KeywordFeature keywordFeature,DistrictFeature districtFeature,ItFeature itFeature){
//      TODO 未写先懵逼
        if (CollectionUtils.isEmpty(adUnitIdSet)){
            return Collections.emptySet();
        }
        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);
        filterKeywordFeature(keywordUnitIdSet,keywordFeature);
        filterDistrictFeature(districtUnitIdSet,districtFeature);
        filterItTagFeature(itUnitIdSet,itFeature);
        return new HashSet<>(CollectionUtils.union(CollectionUtils.union(keywordUnitIdSet,districtUnitIdSet),itUnitIdSet));
    }
    private void filterKeywordFeature(Collection<Long> adUnitIds,KeywordFeature keywordFeature){
//      TODO 懵逼
        if (CollectionUtils.isEmpty(adUnitIds)){
            return;
        }
        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())){
            CollectionUtils.filter(adUnitIds,adUnitId->DataTable.of(UnitKeywordIndex.class).match(adUnitId,keywordFeature.getKeywords()));
        }
    }
    private void filterDistrictFeature(Collection<Long> adUnitIds,DistrictFeature districtFeature){
        if (CollectionUtils.isEmpty(adUnitIds)){
            return;
        }
        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())){
            CollectionUtils.filter(adUnitIds,adUnitId->DataTable.of(UnitDistrictIndex.class).match(adUnitId,districtFeature.getDistricts()));
        }
    }
    private void filterItTagFeature(Collection<Long> adUnitIds,ItFeature itFeature){
        if (CollectionUtils.isEmpty(adUnitIds)){
            return;
        }
        if (CollectionUtils.isNotEmpty(itFeature.getIts())){
            CollectionUtils.filter(adUnitIds,adUnitId->DataTable.of(UnitItIndex.class).match(adUnitId,itFeature.getIts()));
        }
    }
}
