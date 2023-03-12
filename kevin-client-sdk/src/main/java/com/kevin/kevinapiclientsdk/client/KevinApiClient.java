package com.kevin.kevinapiclientsdk.client;


import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.kevin.kevinapiclientsdk.model.User;
import com.kevin.kevinapiclientsdk.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Projectname: kevinapi-backend
 * @Filename: KevinApiClient
 * @Author: skw
 */


public class KevinApiClient {
	//网关地址
	public static final String GATEWAY_HOST = "http://101.43.207.192:8090";

	//AK/SK
	private String accessKey;

	private String secretKey;

	public KevinApiClient(String accessKey, String secretKey) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	/**
	 * 用到了hutool工具包
	 * 没什么技巧直接调用
	 */
	public String getNameByGet(String name) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("username", name);
		String result = HttpUtil.get(GATEWAY_HOST + "api/name", paramMap);

		System.out.println(result);
		return result;
	}

	public String getNameByPost(String name) {
		//可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", name);
		String result = HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);

		System.out.println(result);
		return result;
	}


	private Map<String, String> getHeaderMap(String body) {
		Map<String, String> header = new HashMap<>();
		header.put("accessKey", accessKey);

		//放入随机数
		String nonce = UUID.randomUUID().toString();
		//String nonce = "skw";

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

	public String getUsernameByPost(User user) {
		String json = JSONUtil.toJsonStr(user);

		HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
				.addHeaders(getHeaderMap(json))
				.body(json)
				.execute();
		System.out.println(httpResponse.getStatus());
		String result = httpResponse.body();
		System.out.println(result);
		return result;
	}

}