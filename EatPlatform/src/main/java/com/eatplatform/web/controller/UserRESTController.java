package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		log.info("checkUserByUserId()");
		log.info(userId);
		int result = userService.checkUserByUserId(userId);
		log.info(result);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	@PostMapping("/modify/password")
	public ResponseEntity<Map<String, String>> modifyUserPw(@RequestBody UserVO userVO, HttpServletRequest request) {
		log.info("modifyUserPw()");
		UserVO vo = userVO;
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		String userEmail = userVO.getUserEmail();
		log.info("userId : " + userId);
		log.info("email : " + userEmail);
		
		vo.setUserId(userId);
		vo.setUserEmail(userEmail);
		int result = userService.modifyUserPw(vo);
		
		Map<String, String> map = new HashMap<>();
		map.put("result", Integer.toString(result));
		
		if(result == 1) {
			map.put("message", "비밀번호가 변경 되었습니다. 다시 로그인 해주세요.");
			if(userId != null) {
				session.invalidate();				
			}
		} else {
			map.put("message", "비밀번호 변경에 실패하였습니다.");
		}
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}
	
}
