package com.imooc.ad.service.impl;

import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUnitRepository;
import com.imooc.ad.dao.CreativeRepository;
import com.imooc.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.imooc.ad.dao.unit_condition.AdUnitItRepository;
import com.imooc.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.imooc.ad.dao.unit_condition.CreativeUnitRepository;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUnit;
import com.imooc.ad.entity.unit_condition.AdUnitDistrict;
import com.imooc.ad.entity.unit_condition.AdUnitIt;
import com.imooc.ad.entity.unit_condition.AdUnitKeyword;
import com.imooc.ad.entity.unit_condition.CreativeUnit;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IAdUnitService;
import com.imooc.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;

    private final AdUnitKeywordRepository keywordRepository;
    private final AdUnitItRepository itRepository;
    private final AdUnitDistrictRepository districtRepository;
    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;

    public AdUnitServiceImpl(AdPlanRepository planRepository, AdUnitRepository unitRepository,
                             AdUnitKeywordRepository keywordRepository, AdUnitItRepository itRepository,
                             AdUnitDistrictRepository districtRepository, CreativeRepository creativeRepository,
                             CreativeUnitRepository creativeUnitRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.keywordRepository = keywordRepository;
        this.itRepository = itRepository;
        this.districtRepository = districtRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

    /**
     * 创建推广单元
     *
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if (!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional<AdPlan> adPlan = planRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        AdUnit oldUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (oldUnit != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }
        AdUnit adUnit = unitRepository.save(new AdUnit(request.getPlanId(), request.getUnitName(),
                request.getPositionType(), request.getBudget()));
        return new AdUnitResponse(adUnit.getId(),adUnit.getUnitName());
    }

    /**
     * 创建关键词限制维度
     *
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitKeywordResponse createAdUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream().map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());
//      验证推广单元id是否存在
        if (!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())){
            request.getUnitKeywords().forEach(i->unitKeywords.add(new AdUnitKeyword(i.getUnitId(),i.getKeyword())));
            ids = keywordRepository.saveAll(unitKeywords).stream().map(AdUnitKeyword::getId).collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    /**
     * 创建兴趣维度限制
     *
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitItResponse createAdUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitItIds = request.getUnitIts().stream().map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitItIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitIt> adUnitIts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitIts())){
            request.getUnitIts().forEach(i->adUnitIts.add(new AdUnitIt(i.getUnitId(),i.getItTag())));
            ids = itRepository.saveAll(adUnitIts).stream().map(AdUnitIt::getId).collect(Collectors.toList());
        }
        return new AdUnitItResponse(ids);
    }

    /**
     * 创建地域维度限制
     *
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public AdUnitDistrictResponse createAdDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> districtIds = request.getUnitDistricts().stream().map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(districtIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitDistrict> adUnitDistricts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitDistricts())){
            request.getUnitDistricts().forEach(i->adUnitDistricts.add(new AdUnitDistrict(i.getUnitId()
                    ,i.getProvince(),i.getCity())));
            ids = districtRepository.saveAll(adUnitDistricts).stream().map(AdUnitDistrict::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitDistrictResponse(ids);
    }

    /**
     * 创建创意与推广单元关联
     *
     * @param request
     * @return
     * @throws AdException
     */
    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> unitIds = request.getUnitItems().stream().map(CreativeUnitRequest
                .CreativeUnitItem::getUnitId).collect(Collectors.toList());
        List<Long> creativeIds = request.getUnitItems().stream().map(CreativeUnitRequest
                .CreativeUnitItem::getCreativeId).collect(Collectors.toList());
        if (!isRelatedCreativeExist(creativeIds)|| !isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getUnitItems().forEach(i->creativeUnits.add(new CreativeUnit(i.getCreativeId(),i.getUnitId())));
        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits).stream().map(CreativeUnit::getId)
                .collect(Collectors.toList());
        return new CreativeUnitResponse(ids);
    }
    private boolean isRelatedCreativeExist(List<Long> creativeIds){
        if (CollectionUtils.isEmpty(creativeIds)){
            return false;
        }
        return creativeRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();
    }
    private boolean isRelatedUnitExist(List<Long> unitIds){
        if (CollectionUtils.isEmpty(unitIds)){
            return false;
        }
        return unitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }
}
