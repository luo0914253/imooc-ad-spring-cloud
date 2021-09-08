package com.imooc.ad.service.impl;

import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdUserRepository;
import com.imooc.ad.entity.AdUser;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IUserService;
import com.imooc.ad.utils.CommonUtils;
import com.imooc.ad.vo.CreateUserRequest;
import com.imooc.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final AdUserRepository adUserRepository;

    public UserServiceImpl(AdUserRepository adUserRepository) {
        this.adUserRepository = adUserRepository;
    }

    /**
     * 创建用户
     *
     * @param request
     * @return
     * @throws AdException
     */
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {
        if (!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdUser oldUser = adUserRepository.findByUsername(request.getUsername());
        if (oldUser != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }
        AdUser adUser = adUserRepository.save(new AdUser(request.getUsername(),
                CommonUtils.md5(request.getUsername())));
        return new CreateUserResponse(adUser.getId(),adUser.getUsername(),
                adUser.getToken(),adUser.getCreateTime(),adUser.getUpdateTime());
    }
}
