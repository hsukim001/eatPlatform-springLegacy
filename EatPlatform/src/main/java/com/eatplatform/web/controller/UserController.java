package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.BusinessRequestService;
import com.eatplatform.web.service.UserAdminService;
import com.eatplatform.web.service.UserService;
import com.eatplatform.web.service.UserStoreService;
import com.eatplatform.web.util.PageMaker;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/user")
@Log4j
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserStoreService userStoreService;
	
	@Autowired
	private UserAdminService userAdminService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private BusinessRequestService businessRequestService;
	
	// 회원 가입 페이지 이동
	@GetMapping("/register")
	public void registerGET(Model model) {
		log.info("registerGET()");
	}
	
	// 회원 등록
	@PostMapping("/created")
	public String created(UserVO userMemberVO, RedirectAttributes redirectAttributes, Model model) {
		log.info("created()");
		log.info(userMemberVO);
		String password = userMemberVO.getPassword();
		String encodePassword = passwordEncoder.encode(password);
		userMemberVO.setPassword(encodePassword);
		
		int result = userService.createdUser(userMemberVO);
		
		String message;
		String url;
		if(result == 1) {
			log.info("회원 등록 성공");
			message = "회원 가입에 성공하였습니다.";
			url = "/";
			model.addAttribute("message", message);
		} else {
			log.info("회원 가입 실패");
			message = "회원 가입에 실패하였습니다.";
			url = "/user/register";
		}
		
		redirectAttributes.addAttribute("message", message);
		redirectAttributes.addAttribute("url", url);
		
		return "redirect:/common/message/check";	
	}
	
	// 회원 상세(수정) 페이지 이동
	@GetMapping("/detail")
	public void detail(Model model, @AuthenticationPrincipal CustomUser customUser) {
		log.info("detail()");
		
		String auth = customUser.getAuthorities().toString();
		int userId = customUser.getUser().getUserId();
		UserVO vo = new UserVO();
		
		if(auth.contains("ROLE_MEMBER")) {
			vo = userService.searchUser(userId);	
		} else if(auth.contains("ROLE_STORE")) {
			vo = userStoreService.searchUserByUserId(userId);
		} else if(auth.contains("ROLE_ADMIN")) {
			vo = userAdminService.searchUserByUserId(userId);
		}
		
		model.addAttribute("userInfo", vo);
		log.info(vo);
	}
	
	// 회원 정보 수정
	@PostMapping("/modify")
	public String modify(UserVO userMemberVO, @AuthenticationPrincipal CustomUser customUser, RedirectAttributes redirectAttributes) {
		log.info("modify()");
		log.info(userMemberVO);
		
		UserVO vo = userMemberVO;
		vo.setUserId(customUser.getUser().getUserId());
		int result = 0;
		
		String auth = customUser.getAuthorities().toString();
		if(auth.contains("ROLE_MEMBER")) {
			log.info("권한 : MEMBER");
			result = userService.updateUser(vo);
		} else if(auth.contains("ROLE_STORE")) {
			log.info("권한 : STORE");
			result = userStoreService.updateUser(vo);
		} else if(auth.contains("ROLE_ADMIN")) {
			log.info("권한 : ADMIN");
			result = userAdminService.updateUser(vo);
		}
		
		String message;
		String url = "/user/detail";
		if(result == 1) {
			log.info("회원 정보 수정 성공");
			message = "회원 정보 수정이 완료되었습니다.";
		} else {
			message = "회원 정보 수정에 실패하였습니다.";
		}
		redirectAttributes.addAttribute("message", message);
		redirectAttributes.addAttribute("url", url);
		
		return "redirect:/common/message/check";
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
	public String businessRequestForm(Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUser customUser) {
		log.info("businessRequestForm()");
		int userId = customUser.getUser().getUserId();
		
		int businessRequestId = businessRequestService.getBusinessRequestId(userId);
		log.info("businessRequestId : " + businessRequestId);
		
		// 사업자 등록을 신청한 회원을 사업자 등록 신청 상세 화면으로 이동하는 로직
		if(businessRequestId != 0) {
			log.info("사업자 등록 되어있음");
			redirectAttributes.addAttribute("businessRequestId", businessRequestId);
			return "redirect:/user/business/requestInfo";
		}
		
		model.addAttribute("username", customUser.getUsername());
		model.addAttribute("email", customUser.getUser().getEmail());
		model.addAttribute("phone", customUser.getUser().getPhone());
		model.addAttribute("name", customUser.getUser().getName());
		
		return "user/business/requestForm";
	}
	
	// 사업자 등록 신청
	@PostMapping("/business/request")
	public String businessRequest(@AuthenticationPrincipal CustomUser customUser, RedirectAttributes redirectAttributes) {
		log.info("businessRequest()");
		
		int userId = customUser.getUser().getUserId();
		int result = businessRequestService.businessRequest(userId);
		
		String message;
		String url = "/user/business/requestForm";
		if(result == 1) {
			message = "사업자 등록 신청에 성공하였습니다.";
			log.info(message);
			log.info(url);
		} else {
			log.info("사업자 등록 신청 실패");
			message = "사업자 등록 신청에 실패하였습니다.";
			log.info(message);
			log.info(url);
		}
		redirectAttributes.addAttribute("message", message);
		redirectAttributes.addAttribute("url", url);
		
		return "redirect:/common/message/check";
	}
	
	// 사업자 등록 요청 목록 화면 호출
	@GetMapping("/business/requestList")
	public void businessRequestList(Model model, Pagination pagination) {
		log.info("businessRequestList()");
		
		List<JoinBusinessRequestVO> list = businessRequestService.searchBusinessRequestList(pagination);
		int totalCount = businessRequestService.getBusinessRequestTotalCount();
		log.info("businessList : " + list);
		log.info("totalCount : " + totalCount);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		pageMaker.setTotalCount(totalCount);
		
		model.addAttribute("list", list);
		model.addAttribute("pageMaker", pageMaker);
	}
	
	// 사업자 등록 요청 상세 화면 호출
	@GetMapping("/business/requestInfo")
	public void businessRequestInfo(Model model, @RequestParam("businessRequestId") int businessRequestId) {
		log.info("businessRequestInfo()");
		JoinBusinessRequestVO businessRequestInfo = businessRequestService.searchBusinessRequestInfo(businessRequestId);
		model.addAttribute("info", businessRequestInfo);
	}
	
	// 사업자 등록 요청 승인
	@PostMapping("/business/requestInfo")
	public String businessRequestApprovals(@RequestParam("businessRequestId") int businessRequestId, RedirectAttributes redirectAttributes) {
		log.info("businessRequestApprovals()");
		int result = businessRequestService.businessRequestApploval(businessRequestId);
		
		String message;
		String url = "/user/business/requestList";
		if(result == 1) {
			log.info("사업자 등록 요청 승인");
			message = "사업자 등록 요청에 대한 승인이 완료되었습니다.";
		} else {
			log.info("사업자 등록 요청 거부");
			message = "사업자 등록 요청에 대한 승인이 거부되었습니다.";
		}
		log.info(message);
		log.info(url);
		redirectAttributes.addAttribute("message", message);
		redirectAttributes.addAttribute("url", url);
		
		return "redirect:/common/message/check";
	}
	
}
