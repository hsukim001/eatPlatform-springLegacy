package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.BusinessRequestService;
import com.eatplatform.web.service.UserService;
import com.eatplatform.web.util.ResultMsgResponse;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/user")
public class UserRESTController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private BusinessRequestService businessRequestService;
	
	// 아이디 중복 확인
	@GetMapping("/check/{username}/{type}")
	public ResponseEntity<Integer> checkUserByUserId(@PathVariable("username") String username, @PathVariable("type") String type) {
		log.info("checkUserByUserId()");
		log.info(username);
		int result = userService.checkUser(username);
		log.info(result);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 비밀번호 수정
	@PutMapping("/modify/password")
	public ResponseEntity<Map<String, String>> modifyUserPw(@RequestBody UserVO userMemberVO, @AuthenticationPrincipal CustomUser customUser, 
			HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		log.info("modifyUserPw()");
		UserVO vo = userMemberVO;
		String auth = "";
		String encodePassword = passwordEncoder.encode(userMemberVO.getPassword());
		vo.setPassword(encodePassword);
		log.info("vo : " + vo);
		
		if(customUser != null) {
			int userId = customUser.getUser().getUserId();
			auth = customUser.getAuthorities().toString();
			log.info("userId : " + userId);
			vo.setUserId(userId);
		}
		
		int result = userService.modifyPassword(vo, auth);
		
		Map<String, String> map = new HashMap<>();
		map.put("result", Integer.toString(result));
		
		if(result == 1) {
			map.put("message", "비밀번호가 변경 되었습니다. 다시 로그인 해주세요.");
			if(customUser != null) {
				new SecurityContextLogoutHandler().logout(request, response, authentication);			
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
		String userId = userService.searchUsername(email);
		return new ResponseEntity<String>(userId, HttpStatus.OK);
	}
	
	// 회원 탈퇴
	@PutMapping ("/withdrawal")
	public ResponseEntity<ResultMsgResponse> withdrawalUser(@AuthenticationPrincipal CustomUser customUser, 
			HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		log.info("withdrawalUser()");
		int userId = customUser.getUser().getUserId();
		String auth = customUser.getAuthorities().toString();
		
		int result = userService.withdrawalUser(userId, auth);
		String msg = "";
		
		if(result == 1) {
			msg = "회원 탈퇴가 완료되었습니다.";
			
			// Spring Security의 로그아웃 핸들러 -> SecurityContext에 저장되어있는 인증정보 제거하여 현재 사용자와 모든 보안 데이터를 초기화
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		} else {
			msg = "회원 탈퇴에 실패하였습니다.";
		}
		
		ResultMsgResponse resultResponse = new ResultMsgResponse(result, msg);
		
		return new ResponseEntity<ResultMsgResponse>(resultResponse, HttpStatus.OK);
	}
	
	/**
	 * 사업자 등록 요청 상태 변경
	 * @param businessRequestId
	 * @return
	 */
	@PutMapping("/business/request/status/{businessRequestId}/{requestStatus}")
	public ResponseEntity<Integer> updateBusinessRequestStatus(@PathVariable("businessRequestId") int businessRequestId, @PathVariable("requestStatus") String requestStatus) {
		log.info("requestDenialManagement()"); 
		
		boolean isCheck = businessRequestService.isBusinessRequestRoleMemberAndRequestStatusWait(businessRequestId);
		if(!isCheck) {
			return new ResponseEntity<Integer>(0, HttpStatus.BAD_REQUEST);
		}
		
		int result = businessRequestService.updateRequestStatus(businessRequestId, requestStatus);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	/**
	 * 사업자 등록 요청 취소
	 * @param businessRequestId
	 * @return
	 */
	@DeleteMapping("/business/request/cancel/{businessRequestId}")
	public ResponseEntity<Integer> requestCancel(@PathVariable("businessRequestId") int businessRequestId) {
		int result = businessRequestService.businessRequestCancel(businessRequestId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
