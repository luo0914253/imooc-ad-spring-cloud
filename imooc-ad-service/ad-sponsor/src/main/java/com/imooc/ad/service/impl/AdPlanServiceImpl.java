package com.imooc.ad.service.impl;

import com.imooc.ad.constant.CommonStatus;
import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUserRepository;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUser;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IAdPlanService;
import com.imooc.ad.utils.CommonUtils;
import com.imooc.ad.vo.AdPlanGetRequest;
import com.imooc.ad.vo.AdPlanRequest;
import com.imooc.ad.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdPlanServiceImpl implements IAdPlanService {

    private final AdUserRepository userRepository;
    private final AdPlanRepository planRepository;

    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    /**
     * 创建推广计划
     *
     * @param request
     * @return
     */
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {
        if (!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional<AdUser> adUser = userRepository.findById(request.getUserId());
        if (!adUser.isPresent()){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }
        AdPlan oldPlan = planRepository.findByUserIdAndPlanName(request.getId(), request.getPlanName());
        if (oldPlan !=null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);
        }
        AdPlan adPlan = planRepository.save(new AdPlan(request.getUserId(), request.getPlanName()
                , CommonUtils.parseStringDate(request.getStartDate())
                , CommonUtils.parseStringDate(request.getEndDate())));
        return new AdPlanResponse(adPlan.getId(),adPlan.getPlanName());
    }

    /**
     * 获取推广计划
     *
     * @param request
     * @return
     */
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
        if (!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        return planRepository.findAllByIdAndUserId(request.getIds(),request.getUserId());
    }

    /**
     * 更新推广计划
     *
     * @param request
     * @return
     */
    @Transactional
    public AdPlanResponse updateAdplan(AdPlanRequest request) throws AdException {
        if (!request.updateValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan adPlan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (adPlan ==null){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        if (request.getPlanName() != null){
            adPlan.setPlanName(request.getPlanName());
        }
        if (request.getStartDate() != null){
           adPlan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }
        adPlan.setUpdateTime(new Date());
        return new AdPlanResponse(adPlan.getId(),adPlan.getPlanName());
    }

    /**
     * 删除推广计划
     *
     * @param request
     * @throws AdException
     */
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        if (!request.deleteValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan adPlan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (adPlan ==null){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        adPlan.setPlanStatus(CommonStatus.INVALID.getStatus());
        adPlan.setUpdateTime(new Date());
        planRepository.save(adPlan);
    }
}
