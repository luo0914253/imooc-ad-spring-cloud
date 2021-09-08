package com.imooc.ad.service;

import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.*;

public interface IAdUnitService {
    /**
     * 创建推广单元
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    /**
     * 创建关键词限制维度
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitKeywordResponse createAdUnitKeyword(AdUnitKeywordRequest request) throws AdException;

    /**
     * 创建兴趣维度限制
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitItResponse createAdUnitIt(AdUnitItRequest request) throws AdException;

    /**
     * 创建地域维度限制
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitDistrictResponse createAdDistrict(AdUnitDistrictRequest request) throws AdException;

    /**
     * 创意与推广单元关联
     * @param request
     * @return
     * @throws AdException
     */
    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;
}
