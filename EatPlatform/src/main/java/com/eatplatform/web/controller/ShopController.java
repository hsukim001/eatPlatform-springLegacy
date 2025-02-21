package com.eatplatform.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/shop")
@Log4j
public class ShopController {

	

	@GetMapping("/")
	public String root() {
		log.info("Shop, root");

		return "/shop/list";
	}	

	@GetMapping("/list")
	public String list() {
		log.info("Shop, list()");

		return "/shop/list";
	}

}
