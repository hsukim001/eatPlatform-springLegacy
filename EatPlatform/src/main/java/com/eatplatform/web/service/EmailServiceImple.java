package com.eatplatform.web.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.EmailVO;
import com.eatplatform.web.persistence.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class EmailServiceImple implements EmailService{
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	UserMapper userMapper;
	
	@Override
	public EmailVO sendEmailByAuthCode(EmailVO emailVO) {
		
		Random random = new Random();
        int randomCode = random.nextInt(999999);
        
        String authCode = Integer.toString(randomCode);
		
		String setFrom = "hsuemail157@gmail.com";
		String toMail = emailVO.getUserEmail();
		log.info(toMail);
		String subject = "먹플랫폼 회원가입 인증 번호 메일입니다.";
		String content = "회원가입 인증번호는 " + authCode + " 입니다.";
		
		EmailVO vo = emailVO;
		
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper msgHelper = new MimeMessageHelper(msg, true, "UTF-8");
			
			msgHelper.setFrom(setFrom);
			msgHelper.setTo(toMail);
			msgHelper.setSubject(subject);
			msgHelper.setText(content);
			
			mailSender.send(msg);
			
//			LocalDateTime now = LocalDateTime.now();
//			LocalDateTime nowPlusMin = now.plusMinutes(3);
//						
//			String sendTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//			String expirationTime = nowPlusMin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			
			Date sendTime = new Date();
			Date expirationTime = new Date();
			
			vo.setAuthCode(authCode);
			vo.setSendTime(sendTime);
			vo.setExpirationTime(expirationTime);
			vo.setMessage("코드 전송이 완료되었습니다. 3분이내에 인증코드를 확인해주세요.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return vo;
	}

	@Override
	public EmailVO checkAuthCode(EmailVO emailVO, String checkCode) {
		
		int checkEmail = userMapper.checkUserByUserEmail(emailVO.getUserEmail());
		EmailVO vo = emailVO;
		Date sendTime = new Date();

		if(checkEmail == 0) {
			if(vo.getExpirationTime().before(sendTime)) {
				if(vo.getAuthCode() == checkCode) {
					vo.setStatus(1);
					String message = "이메일 인증이 성공";
					vo.setMessage(message);
				} else {
					vo.setStatus(3);
					String message = "인증코드가 맞지 않습니다.";
					vo.setMessage(message);
				}
			} else {
				vo.setStatus(2);
				String message = "인증시간이 만료되었습니다. 인증이메일을 다시 받아주세요.";
				vo.setAuthCode("");
				vo.setExpirationTime(null);
				vo.setMessage(message);
			}
		} else {
			vo.setStatus(1);
			String message = "이미 사용중인 이메일입니다.";
			vo.setMessage(message);
		}
		
		return vo;
	}
	
}
