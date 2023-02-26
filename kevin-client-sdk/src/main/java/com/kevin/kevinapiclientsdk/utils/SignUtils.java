package com.kevin.kevinapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @Projectname: kevinapi-backend
 * @Filename: SignUtils
 * @Author: skw
 */
public class SignUtils {

	/**
	 * 签名生成
	 *
	 * @param body  请求体
	 * @param secretKey sk
	 * @param timestamp 时间戳
	 * @param nonce 随机数
	 * @return
	 */
	public static String getSign(String body, String secretKey, String timestamp, String nonce) {
		Digester md5 = new Digester(DigestAlgorithm.SHA256);
		String content = body + "." + secretKey + "." + timestamp + "." + nonce;
		return md5.digestHex(content);
	}
}
