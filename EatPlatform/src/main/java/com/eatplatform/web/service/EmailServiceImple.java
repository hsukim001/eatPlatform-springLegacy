package com.eatplatform.web.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.EmailVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.persistence.UserMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class EmailServiceImple implements EmailService {

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserMapper userMapper;

	// 회원가입 이메일 인증코드 발송
	@Override
	public EmailVO sendEmailByAuthCode(EmailVO emailVO) {
		log.info("sendEmailByAuthCode()");
		log.info(emailVO.getUserEmail());

		int checkEmail = userMapper.countUserEmail(emailVO.getUserEmail());
		EmailVO vo = new EmailVO();
		
		String mailType = "회원가입";
		if (checkEmail == 0) {
			vo = emailFrom(emailVO.getUserEmail(), mailType);
		} else {
			vo.setStatus(1);
			vo.setMessage("이미 사용중인 이메일 입니다.");
		}

		return vo;
	}

	// 인증코드 확인
	@Override
	public EmailVO checkAuthCode(EmailVO emailVO, String checkCode) {
		log.info("checkAuthCode()");

		EmailVO vo = emailVO;
		Date sendTime = new Date();

		if (vo.getExpirationTime().after(sendTime)) {
			if (passwordEncoder.matches(checkCode, vo.getAuthCode())) {
				vo.setStatus(0);
				String message = "이메일 인증에 성공하였습니다.";
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

		return vo;
	}
	
	// 인증코드 난수 생성
	private String createdAuthCode() {
		Random random = new Random();
		int randomCode = random.nextInt(999999);
		
		log.info("인증번호 : " + randomCode);

		String authCode = Integer.toString(randomCode);
		return authCode;
	}
	
	// 이메일 입력 폼
	private EmailVO emailFrom(String userEmail, String mailType) {
		String authCode = createdAuthCode();
		// authCode 암호화
		String encode = passwordEncoder.encode(authCode);
		
		String setFrom = "hsuemail157@gmail.com";
		String toMail = userEmail;
		log.info(toMail);
		String subject = "먹플랫폼 " + mailType + " 인증번호 메일입니다.";
		String content = mailType + " 인증번호는 " + authCode + " 입니다.";

		EmailVO vo = new EmailVO();

		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper msgHelper = new MimeMessageHelper(msg, true, "UTF-8");

			msgHelper.setFrom(setFrom);
			msgHelper.setTo(toMail);
			msgHelper.setSubject(subject);
			msgHelper.setText(content);

			mailSender.send(msg);
			
			Date sendTime = new Date();
			Date time = new Date();
			
	        Instant instant = time.toInstant();
	        Instant updatedInstant = instant.plus(Duration.ofMinutes(3)); // 3분 추가
	        Date expirationTime = Date.from(updatedInstant);

	        vo.setUserEmail(userEmail);
			vo.setAuthCode(encode);
			vo.setSendTime(sendTime);
			vo.setExpirationTime(expirationTime);
			vo.setMessage("인증메일 전송이 완료되었습니다. 3분이내에 인증코드를 입력해주세요.");
			vo.setStatus(0);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setStatus(5);
			vo.setMessage("메일전송 과정에서 오류가 발생하였습니다.");
		}
		
		return vo;
	}

	// 아이디, 비밀번호 찾기
	@Override
	public EmailVO sendSearchUser(UserVO userMemberVO, String mailType) {
		log.info("sendSearchUser()");
		
		int result = 0;
		EmailVO vo = new EmailVO();
		
		if(mailType.equals("비밀번호")) {
			result = userMapper.countUserEmailByUserIdEmail(userMemberVO);
		} else if(mailType.equals("아이디")) {
			result = userMapper.countUserEmail(userMemberVO.getEmail());
		}
		
		if(result == 1) {
			String userEmail = userMemberVO.getEmail();
			vo = emailFrom(userEmail, mailType);	
		} else {
			vo.setStatus(1);
			vo.setMessage("회원정보가 존재하지 않습니다.");
		}
		
		return vo;
	}

}
