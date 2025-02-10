package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.persistence.UserAdminMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class UserAdminServiceImple implements UserAdminService{
	
	@Autowired
	UserAdminMapper userAdminMapper;

	// 관리자 정보 검색(userId)
	@Override
	public UserVO searchUserByUserId(int userId) {
		log.info("searchUserByUserId()");
		return userAdminMapper.selectUserByUserId(userId);
	}
	
	@Override
	public int updateUser(UserVO userMemberVO) {
		log.info("updateUser()");
		return userAdminMapper.updateUser(userMemberVO);
	}

}
