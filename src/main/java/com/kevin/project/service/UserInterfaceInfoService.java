package com.kevin.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin.kevinapicommon.model.entity.UserInterfaceInfo;

/**
* @author s'l'z
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-02-23 17:38:59
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

	void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

	/**
	 * 调用接口统计
	 * @param interfaceInfoId
	 * @param userId
	 * @return
	 */
	boolean invokeCount(long interfaceInfoId, long userId);
}
