package com.eatplatform.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/page")
@Log4j
public class ReviewController {
	
	@GetMapping("/detail")
	public void detail() {
		log.info("detail()");
	}

}
