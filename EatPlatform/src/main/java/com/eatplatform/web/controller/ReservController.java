package com.eatplatform.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/reserv")
@Log4j
public class ReservController {
	
	// 목록 페이지 호출
	@GetMapping("/list")
	public void reservList() {
		log.info("reservList()");
	}
	
}
