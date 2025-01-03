package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
		int result = userMapper.checkUserByUserId(userId);
		UserVO vo = new UserVO();
		if(result == 1) {
			vo = userMapper.selectUserByUserId(userId);
			String encodePw = vo.getUserPw();
			
			if(!passwordEncoder.matches(userPw, encodePw)) {
				result = 0;
			}
		}
		
		return result;
	}

	// 회원 비밀번호 수정
	@Override
	public int modifyUserPw(UserVO userVO) {
		log.info("modifyUserPw()");
		log.info(userVO);
		UserVO vo = new UserVO();
		
		// 암호화
		String encodePw = passwordEncoder.encode(userVO.getUserPw());
		
		vo.setUserPw(encodePw);
		int result = 0;
		if(userVO.getUserEmail().isBlank() && userVO.getUserId() != null) {
			log.info("userEmail = null");
			vo.setUserId(userVO.getUserId());
			result = userMapper.updateUserPwByUserId(vo);
		} else if(userVO.getUserId() == null && !userVO.getUserEmail().isBlank()) {
			log.info("userId = null");
			vo.setUserEmail(userVO.getUserEmail());
			result = userMapper.updateUserPwByUserEmail(vo);
		}
		return result;
	}

	// 사용자 확인
	@Override
	public int checkUserByUserId(String userId) {
		return userMapper.checkUserByUserId(userId);
	}

	// 아이디 찾기
	@Override
	public String searchUserId(String userEmail) {
		UserVO vo = userMapper.selectUserIdByUserEmail(userEmail);
		String userId = vo.getUserId();
		int index = userId.length() - 2;
		log.info(index);
		String chUserId = userId.substring(0, 2);
		for(int i = 0; i < index; i++) {
			chUserId += "*";
		}
		return chUserId;
	}

	// 회원 삭제
	@Override
	public int deleteUser(char status, String userId) {
		log.info("deleteUser()");
		UserVO vo = new UserVO();
		vo.setUserActiveYn(status);
		vo.setUserId(userId);
		return userMapper.updateActiveYnByUserId(vo);
	}
	
}
