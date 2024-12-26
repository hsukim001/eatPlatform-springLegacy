package com.eatplatform.web.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.EmailVO;
import com.eatplatform.web.persistence.UserMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class EmailServiceImple implements EmailService {

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	UserMapper userMapper;

	@Override
	public EmailVO sendEmailByAuthCode(EmailVO emailVO) {

		int checkEmail = userMapper.checkUserByUserEmail(emailVO.getUserEmail());
		EmailVO vo = new EmailVO();

		if (checkEmail == 0) {
			Random random = new Random();
			int randomCode = random.nextInt(999999);

			String authCode = Integer.toString(randomCode);

			String setFrom = "hsuemail157@gmail.com";
			String toMail = emailVO.getUserEmail();
			log.info(toMail);
			String subject = "먹플랫폼 회원가입 인증 번호 메일입니다.";
			String content = "회원가입 인증번호는 " + authCode + " 입니다.";

			vo = emailVO;

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

				vo.setAuthCode(authCode);
				vo.setSendTime(sendTime);
				vo.setExpirationTime(expirationTime);
				vo.setMessage("코드 전송이 완료되었습니다. 3분이내에 인증코드를 확인해주세요.");
				vo.setStatus(0);
			} catch (Exception e) {
				e.printStackTrace();
				vo.setStatus(5);
				vo.setMessage("메일전송 과정에서 오류가 발생하였습니다.");
			}
		} else {
			vo.setStatus(1);
			vo.setMessage("이미 사용중인 이메일 입니다.");
		}

		return vo;
	}

	@Override
	public EmailVO checkAuthCode(EmailVO emailVO, String checkCode) {
		log.info("checkAuthCode()");

		EmailVO vo = emailVO;
		Date sendTime = new Date();

		if (vo.getExpirationTime().after(sendTime)) {
			if (vo.getAuthCode().equals(checkCode)) {
				vo.setStatus(0);
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

		return vo;
	}

}
