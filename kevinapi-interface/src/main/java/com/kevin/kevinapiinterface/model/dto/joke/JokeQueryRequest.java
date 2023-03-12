package com.kevin.kevinapiinterface.model.dto.joke;

import com.kevin.kevinapiinterface.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * 用户获取笑话请求
 *
 * @Projectname: kevinapi-backend
 * @Filename: JokeGetRequest
 * @Author: skw
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class JokeQueryRequest extends PageRequest implements Serializable {

	/**
	 * 个数
	 */
	private Integer num;

}
