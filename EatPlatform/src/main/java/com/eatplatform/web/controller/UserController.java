package com.eatplatform.web.controller;

import java.util.Date;

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
	public String modify(UserVO userVO) {
		log.info("modify()");
		UserVO vo = userVO;
		vo.setUserId("user"); // session userId (추후 변경)
		int result = userService.modifyUser(vo);
		
		if(result == 1) {
			log.info("회원 정보 수정 성공");
		}
		
		return "redirect:/user/detail";
	}
	
	@GetMapping("/modifyPw")
	public void modifyPwGET() {
		log.info("modifyPwGET()");
	}
	
	@PostMapping("/modifyPw")
	public String modifyPwPOST(String userPw, HttpServletRequest request) {
		log.info("modifyPwPOST()");
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		log.info(userId);
		log.info(userPw);
		
		int result = userService.modifyUserPw(userId, userPw);
		if(result == 1) {
			log.info("비밀번호 수정 완료");
		}
		return "redirect:/user/detail";
	}
	
	// 비밀번호 찾기 화면 호출
	@GetMapping("/searchPw")
	public void searchPw() {
		log.info("searchPw()");
	}
	
	// 이메일 인증 화면 호출
	@GetMapping("/authUser")
	public void authUser() {
		log.info("authUser()");
	}
}
