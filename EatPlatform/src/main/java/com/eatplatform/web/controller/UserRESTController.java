package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.EmailVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/user")
public class UserRESTController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/check/{userId}")
	public ResponseEntity<Integer> checkUserByUserId(@PathVariable("userId") String userId) {
		log.info("checkUser");
		log.info(userId);
		int result = userService.checkUserByUserId(userId);
		log.info(result);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
