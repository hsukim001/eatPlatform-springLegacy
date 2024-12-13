package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.service.StoreService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/store")
@Log4j
public class StoreController {
	
	@Autowired
	private StoreService storeService;
	
	@GetMapping("/newStore")
	public void newStore() {
		log.info("newStore()");
	}
	
	@PostMapping("/register")
	public void register(StoreVO storeVO) {
		int result = storeService.registerStore(storeVO);
	}
}
