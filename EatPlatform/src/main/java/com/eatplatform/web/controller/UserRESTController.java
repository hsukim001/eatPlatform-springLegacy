package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.EmailVO;
import com.eatplatform.web.service.UserService;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/user")
public class UserRESTController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@GetMapping("/check/{userId}")
	public ResponseEntity<Integer> checkUserByUserId(@PathVariable("userId") String userId) {
		log.info("checkUser");
		log.info(userId);
		int result = userService.checkUserByUserId(userId);
		log.info(result);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	@GetMapping("/created/{email}")
	public ResponseEntity<EmailVO> registerCheckEmail(@PathVariable("email") String email) {
		log.info("registerCheckEmail()");
		log.info(email);
		
		Random random = new Random();
		int num = random.nextInt(999999);
		
		StringBuilder sb = new StringBuilder();
		
		String setFrom = "hsuemail157@gmail.com";
		String tomail = email + ".com";
		String title = "먹플랫폼 회원가입 인증 메일입니다.";
		
		sb.append("먹플랫폼 회원가입 인증번호는 " + num + " 입니다.");
		String content = sb.toString();
		
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper msgHelper = new MimeMessageHelper(msg, true, "UTF-8");
			
			msgHelper.setFrom(setFrom);
			msgHelper.setTo(tomail);
			msgHelper.setSubject(title);
			msgHelper.setText(content);
			
			mailSender.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		EmailVO vo = new EmailVO();
		vo.setStatus(true);
		vo.setNum(num);
		
		return new ResponseEntity<EmailVO>(vo, HttpStatus.OK);
		
	}
	
}
