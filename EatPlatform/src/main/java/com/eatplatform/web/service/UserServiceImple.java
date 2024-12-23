package com.eatplatform.web.service;

import java.util.Random;

import javax.mail.MessagingException;
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
public class UserServiceImple implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String encryptPassword(String password) {
		return passwordEncoder.encode(password);
	}

	// 회원 등록
	@Override
	public int createdUser(UserVO userVO, int flagNum) {
		log.info("createdUserList()");
		
		// 비밀번호 암호화
		String password = userVO.getUserPw();
		log.info("암호화 전 : " + password);
		String encodePassword = passwordEncoder.encode(password);
		log.info("암호화 후 : " + encodePassword);
		
		UserVO vo = userVO;
		vo.setUserPw(encodePassword);
		
		// 회원 가입 유형의 따른 권한 설정
		// 1 : 일반 회원
		// 2 : 사업자 회원
		if(flagNum == 1) {
			vo.setUserAuth("ROLE_MEMBER");
		} else if(flagNum == 2) {
			vo.setUserAuth("ROLE_STORE");
		}
		vo.setUserActiveYn('Y');
		
		log.info(vo);
		
		return userMapper.insertUser(vo);
	}

	// 회원 검색(회원 아이디)
	@Override
	public UserVO searchUser(String userId) {
		log.info("searchUserList()");
		UserVO vo = userMapper.selectUserByUserId(userId);
		return vo;
	}

	// 회원 정보 수정(비밀번호 제외)
	@Override
	public int modifyUser(UserVO userVO) {
		log.info("modifyUserList()");
		
		return userMapper.updateUser(userVO);
	}

	// 로그인
	@Override
	public int login(String userId, String userPw) {
		log.info("login()");
		UserVO vo = userMapper.selectUserPwByUserId(userId);
		String encodePw = vo.getUserPw();
		log.info(encodePw);
		
		boolean isMatcher = false;
		int result = 0;
		if(isMatcher = passwordEncoder.matches(userPw, encodePw)) {
			result = 1; 
		}
		log.info(isMatcher);
		
		return result;
	}

	// 회원 비밀번호 수정
	@Override
	public int modifyUserPw(String userId, String userPw) {
		log.info("modifyUserPw()");
		log.info(userPw);
		UserVO vo = new UserVO();
		
		// 암호화
		String encodePw = passwordEncoder.encode(userPw);
		
		vo.setUserId(userId);
		vo.setUserPw(encodePw);
		return userMapper.updateUserPw(vo);
	}

	@Override
	public int checkUserByUserId(String userId) {
		return userMapper.checkUserByUserId(userId);
	}
	
}
