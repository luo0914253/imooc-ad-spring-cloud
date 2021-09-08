package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IAdUnitService;
import com.imooc.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AdUnitOPController {
    private final IAdUnitService adUnitService;

    public AdUnitOPController(IAdUnitService adUnitService) {
        this.adUnitService = adUnitService;
    }
    @PostMapping("/create/adUnit")
    public AdUnitResponse createAdPlan(@RequestBody AdUnitRequest request) throws AdException {
        log.info("ad-sponsor:createAdPlan -> {}", JSON.toJSONString(request));
        return adUnitService.createUnit(request);
    }
    @PostMapping("/create/unitKeyword")
    public AdUnitKeywordResponse createUnitKeyword(@RequestBody AdUnitKeywordRequest request) throws AdException {
        log.info("ad-sponsor:createAdPlan -> {}", JSON.toJSONString(request));
        return adUnitService.createAdUnitKeyword(request);
    }
    @PostMapping("/create/unitIt")
    public AdUnitItResponse createUnitIt(@RequestBody AdUnitItRequest request) throws AdException {
        log.info("ad-sponsor:createAdPlan -> {}", JSON.toJSONString(request));
        return adUnitService.createAdUnitIt(request);
    }
    @PostMapping("/create/unitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(@RequestBody AdUnitDistrictRequest request) throws AdException {
        log.info("ad-sponsor:createAdPlan -> {}", JSON.toJSONString(request));
        return adUnitService.createAdDistrict(request);
    }
    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(@RequestBody CreativeUnitRequest request) throws AdException {
        log.info("ad-sponsor:createAdPlan -> {}", JSON.toJSONString(request));
        return adUnitService.createCreativeUnit(request);
    }
}
