package com.kevin.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.kevin.kevinapicommon.model.entity.InterfaceInfo;
import com.kevin.kevinapicommon.service.InnerInterfaceInfoService;
import com.kevin.project.common.ErrorCode;
import com.kevin.project.exception.BusinessException;
import com.kevin.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
