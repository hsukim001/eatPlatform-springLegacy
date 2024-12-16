package com.eatplatform.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/access")
@Log4j
public class AccessController {
	
	@Autowired
	private UserService userService;
	
	// 로그인 페이지 이동
	@GetMapping("/login")
	public void loginGET() {
		log.info("loginGET()");
	}
	
	// 로그인
	@PostMapping("/login")
	public String userMatcher(String userId, String userPw, HttpServletRequest request) {
		log.info("userMatcher()");
		String matcherURL = "redirect:/access/login";
		String loginMsg = "로그인 실패";
		
		int result = userService.login(userId, userPw);
		if(result == 1) {
			loginMsg = "로그인 성공";
			matcherURL = "redirect:/";
			// 로그인 세션 부여
			HttpSession session = request.getSession();
			session.setAttribute("userId", userId);
			session.setMaxInactiveInterval(600);
		}
		log.info(loginMsg);
		return matcherURL;
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		log.info("logout()");
		
		HttpSession session = request.getSession();
		session.invalidate();
		
		return "redirect:/";
	}
}
