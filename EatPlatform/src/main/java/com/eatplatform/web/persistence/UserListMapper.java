package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserListVO;

@Mapper
public interface UserListMapper {
	UserListVO selectUserListByUserId(String userId);
	int insertUserList(UserListVO userListVO);
	int updateUserList(UserListVO userListVO);
	int deleteUserList(String userId);
}
