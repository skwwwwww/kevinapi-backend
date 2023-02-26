package com.kevin.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevin.kevinapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author s'l'z
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-02-23 17:38:59
* @Entity com.kevin.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

	List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




