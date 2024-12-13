package com.eatplatform.web.service;

import com.eatplatform.web.domain.UserListVO;

public interface UserListService {
	int createdUserList(UserListVO userListVO, int flagNum);
	
	UserListVO searchUserList(String userId);
	
	int modifyUserList(UserListVO userListVO);
}
