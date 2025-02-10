package com.eatplatform.web.service;

import com.eatplatform.web.domain.UserVO;

public interface UserAdminService {
	// 관리자 정보 검색(userId)
	UserVO searchUserByUserId(int userId);
	// 관리자 정보 수정
	int updateUser(UserVO userMemberVO);
}
