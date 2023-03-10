package com.kevin.project.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kevin.kevinapiclientsdk.client.KevinApiClient;
import com.kevin.kevinapiclientsdk.utils.SignUtils;
import com.kevin.kevinapicommon.model.entity.InterfaceInfo;
import com.kevin.kevinapicommon.model.entity.User;
import com.kevin.kevinapicommon.model.enums.InterfaceInfoStatusEnum;
import com.kevin.project.annotation.AuthCheck;
import com.kevin.project.common.*;
import com.kevin.project.constant.CommonConstant;
import com.kevin.project.exception.BusinessException;
import com.kevin.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.kevin.project.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.kevin.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.kevin.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.kevin.project.service.InterfaceInfoService;
import com.kevin.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口管理
 *
 * @author yupi
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

	//网关地址
	public static final String GATEWAY_HOST = "http://localhost:8090";

	@Resource
	private InterfaceInfoService interfaceInfoService;

	@Resource
	private UserService userService;

	@Resource
	private KevinApiClient kevinApiClient;

	// region 增删改查

	/**
	 * 创建
	 *
	 * @param interfaceInfoAddRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
		if (interfaceInfoAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
		// 校验
		interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
		User loginUser = userService.getLoginUser(request);
		interfaceInfo.setUserId(loginUser.getId().toString());
		boolean result = interfaceInfoService.save(interfaceInfo);
		if (!result) {
			throw new BusinessException(ErrorCode.OPERATION_ERROR);
		}
		long newInterfaceInfoId = interfaceInfo.getId();
		return ResultUtils.success(newInterfaceInfoId);
	}

	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.getLoginUser(request);
		long id = deleteRequest.getId();
		// 判断是否存在
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可删除
		if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = interfaceInfoService.removeById(id);
		return ResultUtils.success(b);
	}

	/**
	 * 更新
	 *
	 * @param interfaceInfoUpdateRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/update")
	public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
	                                                 HttpServletRequest request) {
		if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
		// 参数校验
		interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
		User user = userService.getLoginUser(request);
		long id = interfaceInfoUpdateRequest.getId();
		// 判断是否存在
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可修改
		if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = interfaceInfoService.updateById(interfaceInfo);
		return ResultUtils.success(result);
	}

	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
		return ResultUtils.success(interfaceInfo);
	}

	/**
	 * 获取列表（仅管理员可使用）
	 *
	 * @param interfaceInfoQueryRequest
	 * @return
	 */
	@AuthCheck(mustRole = "admin")
	@GetMapping("/list")
	public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
		InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
		if (interfaceInfoQueryRequest != null) {
			BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
		}
		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
		List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
		return ResultUtils.success(interfaceInfoList);
	}

	/**
	 * 分页获取列表
	 *
	 * @param interfaceInfoQueryRequest
	 * @param request
	 * @return
	 */
	@GetMapping("/list/page")
	public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
		if (interfaceInfoQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
		long current = interfaceInfoQueryRequest.getCurrent();
		long size = interfaceInfoQueryRequest.getPageSize();
		String sortField = interfaceInfoQueryRequest.getSortField();
		String sortOrder = interfaceInfoQueryRequest.getSortOrder();
		String description = interfaceInfoQuery.getDescription();
		// description 需支持模糊搜索
		interfaceInfoQuery.setDescription(null);
		// 限制爬虫
		if (size > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
		queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
		queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
				sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
		Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
		return ResultUtils.success(interfaceInfoPage);
	}

	// endregion

	/**
	 * 发布
	 *
	 * @param idRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/online")
	@AuthCheck(mustRole = "admin")
	public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,
	                                                 HttpServletRequest request) {
		if (idRequest == null || idRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long id = idRequest.getId();
		// 判断是否存在
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 判断该接口是否可以调用
		com.kevin.kevinapiclientsdk.model.User user = new com.kevin.kevinapiclientsdk.model.User();
		user.setUsername("test");
		String username = kevinApiClient.getUsernameByPost(user);
		if (StringUtils.isBlank(username)) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
		}
		// 仅本人或管理员可修改
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setId(id);
		interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
		boolean result = interfaceInfoService.updateById(interfaceInfo);
		return ResultUtils.success(result);
	}

	/**
	 * 下线
	 *
	 * @param idRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/offline")
	@AuthCheck(mustRole = "admin")
	public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,
	                                                  HttpServletRequest request) {
		if (idRequest == null || idRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long id = idRequest.getId();
		// 判断是否存在
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可修改
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setId(id);
		interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
		boolean result = interfaceInfoService.updateById(interfaceInfo);
		return ResultUtils.success(result);
	}

	private Map<String, String> getHeaderMap(String body, String accessKey, String secretKey) {
		Map<String, String> header = new HashMap<>();
		header.put("accessKey", accessKey);

		//放入随机数
		String nonce = UUID.randomUUID().toString();

		header.put("nonce", nonce.toString());
		//body, 用于在网关生成签名
		header.put("body", body);
		//时间戳
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		header.put("timestamp", timestamp);
		//生成签名
		header.put("sign", SignUtils.getSign(body, secretKey, timestamp, nonce));

		return header;
	}

	/**
	 * 调用
	 *
	 * @param interfaceInfoInvokeRequest 接口参数
	 * @param request 请求
	 * @return
	 */
	@PostMapping("/invoke")
	public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
	                                                HttpServletRequest request) {
		if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long id = interfaceInfoInvokeRequest.getId();
		String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
		// 判断是否存在
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
		}
		User loginUser = userService.getLoginUser(request);
		String accessKey = loginUser.getAccessKey();
		String secretKey = loginUser.getSecretKey();

		//根据id调用
		String url = oldInterfaceInfo.getUrl();
		HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + url)
				.addHeaders(getHeaderMap(userRequestParams, accessKey, secretKey))
				.body(userRequestParams)
				.execute();


		String result = httpResponse.body();


		return ResultUtils.success(result);
	}

}
