package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.IgnoreResponseAdvice;
import com.imooc.ad.client.SponsorClient;
import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.client.vo.AdPlanGetRequest;
import com.imooc.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class SearchController {

    private final RestTemplate restTemplate;
    private final SponsorClient sponsorClient;

    public SearchController(RestTemplate restTemplate, SponsorClient sponsorClient) {
        this.restTemplate = restTemplate;
        this.sponsorClient = sponsorClient;
    }
    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlanByRebbon(@RequestBody AdPlanGetRequest request){
        log.info("ad-search: getAdPlanByRebbon->{}", JSON.toJSONString(request));
//        return restTemplate.postForEntity("http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan"
//                ,request,CommonResponse.class).getBody();
        return sponsorClient.getAdPlans(request);
    }
}
