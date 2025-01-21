package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.BusinessRequestInfoVO;
import com.eatplatform.web.domain.BusinessRequestVO;
import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.UserService;
import com.eatplatform.web.util.PageMaker;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/user")
@Log4j
public class UserController {
	
	@Autowired
	private UserService userService;
	
	// 회원 가입 페이지 이동
	@GetMapping("/register")
	public void registerGET(Model model) {
		log.info("registerGET()");		
	}
	
	// 회원 등록
	@PostMapping("/created")
	public String created(UserVO userVO) {
		log.info("created()");
		log.info(userVO);
		
		int result = userService.createdUser(userVO);
		
		if(result == 1) {
			log.info("회원 등록 성공");
		}
		
		return "redirect:/";
	}
	
	// 회원 상세(수정) 페이지 이동
	@GetMapping("/detail")
	public void detail(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		log.info("detail()");
		String userId = userDetails.getUsername();
		
		UserVO vo = userService.searchUser(userId);
		model.addAttribute("vo", vo);
		log.info(vo);
	}
	
	// 회원 정보 수정
	@PostMapping("/modify")
	public String modify(UserVO userVO) {
		log.info("modify()");
		UserVO vo = userVO;
		log.info(vo);
		int result = userService.modifyUser(vo);
		
		if(result == 1) {
			log.info("회원 정보 수정 성공");
		}
		
		return "redirect:/user/detail";
	}
	
	// 비밀번호 수정 페이지 호출
	@GetMapping("/modifyPw")
	public void modifyPw(@RequestParam(value = "email", required = false) String userEmail, Model model) {
		log.info("modifyPw()");
		log.info("email : " + userEmail);
		if(userEmail != null) {
			model.addAttribute("email", userEmail);		
		}
	}
	
	// 비밀번호 찾기 화면 호출
	@GetMapping("/searchPw")
	public void searchPw() {
		log.info("searchPw()");
	}
	
	// 아이디 찾기 화면 호출
	@GetMapping("/searchId")
	public void searchId() {
		log.info("searchId()");
	}
	
	// 사업자 등록 신청 화면 호출
	@GetMapping("/business/requestForm")
	public void businessRequestForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		log.info("businessRequestForm()");
		String userId = userDetails.getUsername();
		UserVO vo = userService.searchUser(userId);
		
		model.addAttribute("ownerName", vo.getUserName());
	}
	
	// 사업자 등록 신청
	@PostMapping("/business/request")
	public String businessRequest(StoreVO storeVO, StoreAddressVO storeAddressVO, @AuthenticationPrincipal UserDetails userDetails) {
		log.info("businessRequest()");
		String userId = userDetails.getUsername();
		storeVO.setUserId(userId);
		log.info("userId : " + storeVO.getUserId());
		int result = userService.businessRequest(storeVO, storeAddressVO);
		if(result == 1) {
			log.info("사업자 등록 신청 성공");
		}
		return "redirect:/";
	}
	
	// 사업자 등록 요청 목록 화면 호출
	@GetMapping("/business/requestList")
	public void businessRequestList(Model model, Pagination pagination) {
		log.info("businessRequestList()");
		
		List<BusinessRequestInfoVO> list = userService.searchBusinessRequestList(pagination);
		int totalCount = userService.getBusinessRequestTotalCount();
//		PageMaker
	}
	
	// 사업자 등록 요청 상세 화면 호출
	@GetMapping("/business/requestInfo")
	public void businessRequestInfo(Model model, @RequestParam("businessRequestId") int businessRequestId) {
		log.info("businessRequestInfo()");
		BusinessRequestInfoVO businessRequestInfo = userService.searchBusinessRequestInfo(businessRequestId);
		model.addAttribute("info", businessRequestInfo);
	}
	
}
