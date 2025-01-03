package com.eatplatform.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/user")
@Log4j
public class UserController {
	
	@Autowired
	private UserService userService;
	
	// 회원 가입 유형 선택 페이지 이동
	@GetMapping("/flag")
	public void flag() {
		log.info("flag()");
	}
	
	// 회원 가입 페이지 이동
	@GetMapping("/register")
	public void registerGET(Model model, int flagNum) {
		log.info("registerGET()");
		
		model.addAttribute("flagNum", flagNum);
	}
	
	// 회원 등록
	@PostMapping("/register")
	public String registerPOST(UserVO userVO, int flagNum) {
		log.info("registerPOST()");
		log.info(flagNum);
		
		int result = userService.createdUser(userVO, flagNum);
		
		if(result == 1) {
			log.info("회원 등록 성공");
		}
		
		return "redirect:/";
	}
	
	// 회원 상세(수정) 페이지 이동
	@GetMapping("/detail")
	public void detail(Model model, HttpServletRequest request) {
		log.info("detail()");
		HttpSession session = request.getSession();
		
		String userId = (String) session.getAttribute("userId");
		log.info(userId);
		
		UserVO vo = userService.searchUser(userId);
		model.addAttribute("vo", vo);
		log.info(vo);
	}
	
	// 회원 정보 수정
	@PostMapping("/modify")
	public String modify(UserVO userVO, HttpServletRequest request) {
		log.info("modify()");
		UserVO vo = userVO;
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		vo.setUserId(userId);
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
	
	// 이메일 인증 화면 호출
	@GetMapping("/authUser")
	public void authUser(@RequestParam("userId") String userId, @RequestParam String userEmail, Model model) {
		log.info("authUser()");
		model.addAttribute("userId", userId);
		model.addAttribute("userEmail", userEmail);
	}
	
	// 아이디 찾기 화면 호출
	@GetMapping("/searchId")
	public void searchId() {
		log.info("searchId()");
	}
	
}
