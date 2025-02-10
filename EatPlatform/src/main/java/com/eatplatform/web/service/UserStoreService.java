package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.UserVO;

public interface UserStoreService {
	// 회원 정보 검색(userId)
	UserVO searchUserByUserId(int userId);
	// 사업자 회원 정보 수정
	int updateUser(UserVO userMemberVO);
}
