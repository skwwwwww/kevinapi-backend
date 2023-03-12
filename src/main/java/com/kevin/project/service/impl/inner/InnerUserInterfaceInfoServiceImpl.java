package com.kevin.project.service.impl.inner;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kevin.kevinapicommon.model.entity.UserInterfaceInfo;
import com.kevin.kevinapicommon.service.InnerUserInterfaceInfoService;
import com.kevin.project.common.ErrorCode;
import com.kevin.project.exception.BusinessException;
import com.kevin.project.mapper.UserInterfaceInfoMapper;
import com.kevin.project.service.UserInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public UserInterfaceInfo getUserInterfaceInfo(String interfaceInfoId, String userId) {
        if (StringUtils.isAnyBlank(interfaceInfoId, userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("method", userId);
        return userInterfaceInfoMapper.selectOne(queryWrapper);
    }

//    @Override
//    public UserInterfaceInfo getUserInterfaceInfo(String interfaceInfoId, String userId) {
//        if (StringUtils.isAnyBlank(interfaceInfoId, userId)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
//        queryWrapper.eq("method", userId);
//        return userInterfaceInfoMapper.selectOne(queryWrapper);
//    }
}
