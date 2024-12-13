package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.UserListVO;
import com.eatplatform.web.persistence.UserListMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class UserListServiceImple implements UserListService{
	
	@Autowired
	private UserListMapper userListMapper;

	// 회원 등록
	@Override
	public int createdUserList(UserListVO userListVO, int flagNum) {
		log.info("createdUserList()");
		
		UserListVO vo = userListVO;
		
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
		
		return userListMapper.insertUserList(vo);
	}

	// 회원 검색(회원 아이디)
	@Override
	public UserListVO searchUserList(String userId) {
		log.info("searchUserList()");
		UserListVO vo = userListMapper.selectUserListByUserId(userId);
		return vo;
	}

	// 회원 정보 수정
	@Override
	public int modifyUserList(UserListVO userListVO) {
		log.info("modifyUserList()");
		
		return userListMapper.updateUserList(userListVO);
	}
	
}
