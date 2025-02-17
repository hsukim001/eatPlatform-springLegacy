package com.eatplatform.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/common")
@Log4j
public class CommonController {
	
	// alert 페이지 호출
	@GetMapping("/message/check")
	public void messageCheck(Model model, @RequestParam String message, @RequestParam String url) {
		log.info("messageCheck");
		model.addAttribute("message", message);
		model.addAttribute("url", url);
	}
	
}
