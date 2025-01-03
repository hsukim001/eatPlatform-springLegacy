package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.UserService;
import com.eatplatform.web.util.ResultMsgResponse;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/user")
public class UserRESTController {
	
	@Autowired
	private UserService userService;
	
	// 회원 상세 정보 조회
	@GetMapping("/info")
	public ResponseEntity<UserVO> searchUserInfo(HttpServletRequest request) {
		log.info("searchUserInfo()");
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		UserVO vo = userService.searchUser(userId);
		
		return new ResponseEntity<UserVO>(vo, HttpStatus.OK);
	}
	
	// 아이디 확인
	@GetMapping("/check/{userId}")
	public ResponseEntity<Integer> checkUserByUserId(@PathVariable("userId") String userId) {
		log.info("checkUserByUserId()");
		log.info(userId);
		int result = userService.checkUserByUserId(userId);
		log.info(result);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 비밀번호 수정
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
	
	// 아이디 찾기
	@GetMapping("/search/id/{email}/")
	public ResponseEntity<String> searchId(@PathVariable("email") String email) {
		log.info("searchId()");
		String userId = userService.searchUserId(email);
		return new ResponseEntity<String>(userId, HttpStatus.OK);
	}
	
	// 회원 삭제
	@PutMapping ("/delete/{status}")
	public ResponseEntity<ResultMsgResponse> deleteUser(@PathVariable("status") char status, HttpServletRequest request) {
		log.info("deleteUser()");
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		
		int result = userService.deleteUser(status, userId);
		String msg = "";
		
		if(result == 1) {
			msg = "회원 탈퇴가 완료되었습니다.";
		} else {
			msg = "회원 탈퇴에 실패하였습니다.";
		}
		
		ResultMsgResponse response = new ResultMsgResponse(result, msg);
		
		session.invalidate();
		return new ResponseEntity<ResultMsgResponse>(response, HttpStatus.OK);
	}
	
}
