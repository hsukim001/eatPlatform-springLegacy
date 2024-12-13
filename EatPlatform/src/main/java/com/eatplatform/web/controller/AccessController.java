package com.eatplatform.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/access")
@Log4j
public class AccessController {
	
	// 로그인 페이지 이동
	@GetMapping("login")
	public void loginGET() {
		log.info("loginGET()");
	}
}
