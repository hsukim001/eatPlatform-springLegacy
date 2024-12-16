package com.eatplatform.web.service;

import com.eatplatform.web.domain.UserVO;

public interface UserService {
	int createdUserList(UserVO userVO, int flagNum);
	
	UserVO searchUserList(String userId);
	
	int modifyUserList(UserVO userVO);
	
	int login(String userId, String userPw);
	
	int modifyUserPw(String userId, String userPw);
}
