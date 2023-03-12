package com.kevin.kevinapiinterface.controller;

import com.kevin.kevinapiinterface.common.ErrorCode;
import com.kevin.kevinapiinterface.exception.BusinessException;
import com.kevin.kevinapiinterface.mapper.JokeMapper;
import com.kevin.kevinapiinterface.model.dto.joke.JokeQueryRequest;
import com.kevin.kevinapiinterface.model.entity.Joke;
import com.kevin.kevinapiinterface.service.JokeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Projectname: kevinapi-backend
 * @Filename: JokeController
 * @Author: skw
 */
@RestController
@RequestMapping("/joke")
public class JokeController {

	@Resource
	private JokeService jokeService;

	@Resource
	private JokeMapper jokeMapper;

	/**
	 * 获取笑话
	 * @param jokeQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/get")
	public List<com.kevin.kevinapiclientsdk.model.Joke> getJokeByget(@RequestBody JokeQueryRequest jokeQueryRequest, HttpServletRequest request) {


		if(jokeQueryRequest.getNum()>20){
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}


		List<Joke> jokes = jokeMapper.getJoke(jokeQueryRequest.getNum());

		ArrayList<com.kevin.kevinapiclientsdk.model.Joke> jokes1 = new ArrayList<>();
		jokes.forEach((joke -> {
			com.kevin.kevinapiclientsdk.model.Joke joke1 = new com.kevin.kevinapiclientsdk.model.Joke();
			joke1.setContent(joke.getJokeData());
			jokes1.add(joke1);
		}));

		return jokes1;
	}
}
