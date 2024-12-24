package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.EmailVO;
import com.eatplatform.web.service.EmailService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/email")
@Log4j
public class EmailRESTController {
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("/send/authCode")
	public ResponseEntity<EmailVO> sendEmailByAuthCode(@RequestBody EmailVO emailVO) {
		log.info("registerCheckEmail()");
		log.info(emailVO.getUserEmail());
		
		EmailVO vo = emailService.sendEmailByAuthCode(emailVO);
		
		return new ResponseEntity<EmailVO>(vo, HttpStatus.OK);
		
	}
	
	@GetMapping("/check/authCode/{checkCode}")
	public ResponseEntity<EmailVO> checkEmailByAuthCode(@PathVariable("checkCode") String checkCode, @RequestBody EmailVO emailVO) {
		log.info("checkEmailByAuthCode");
		EmailVO vo = emailService.checkAuthCode(emailVO, checkCode);
		return new ResponseEntity<EmailVO>(vo, HttpStatus.OK);
	}
	
}
