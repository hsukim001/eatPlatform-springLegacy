package com.eatplatform.web.service;

import com.eatplatform.web.domain.UserVO;

public interface UserService {
	int createdUser(UserVO userVO, int flagNum);
	
	UserVO searchUser(String userId);
	
	int modifyUser(UserVO userVO);
	
	int login(String userId, String userPw);
	
	int modifyUserPw(String userId, String userPw);
}
