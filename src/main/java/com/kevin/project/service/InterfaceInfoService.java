package com.kevin.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin.kevinapicommon.model.entity.InterfaceInfo;

/**
* @author s'l'z
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2023-02-23 17:38:59
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

	void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
