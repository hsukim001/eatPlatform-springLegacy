package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.persistence.UserStoreMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class UserStoreServiceImple implements UserStoreService{
	
	@Autowired
	UserStoreMapper userStoreMapper;
	
	// 사업자 회원 정보 검색(userId)
	@Override
	public UserVO searchUserByUserId(int userId) {
		log.info("searchUserByUserId()");
		return userStoreMapper.selectUserByUserId(userId);
	}
	
	// 사업자 회원 수정
	@Override
	public int updateUser(UserVO userMemberVO) {
		log.info("updateUser()");
		return userStoreMapper.updateUser(userMemberVO);
	}
	
}
