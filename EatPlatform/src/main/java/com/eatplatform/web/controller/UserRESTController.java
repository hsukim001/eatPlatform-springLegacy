package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/user")
public class UserRESTController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/check/{userId}")
	public ResponseEntity<Integer> checkUserByUserId(@PathVariable("userId") String userId) {
		log.info("checkUser");
		log.info(userId);
		int result = userService.checkUserByUserId(userId);
		log.info(result);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	@GetMapping("/search/{userId}/{userEmail}")
	public ResponseEntity<Integer> searchUserByUserIdUserEmail(@PathVariable("userId") String userId, @PathVariable("userEmail") String userEmail) {
		log.info("searchUserByUserIdUserEmail()");
		UserVO userVO = new UserVO();
		userVO.setUserId(userId);
		userVO.setUserEmail(userEmail);
		int result = userService.checkUserByUserIdUserEmail(userVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
