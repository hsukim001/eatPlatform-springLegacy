package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserVO;

@Mapper
public interface UserMapper {
	UserVO selectUserListByUserId(String userId);
	int insertUserList(UserVO userVO);
	int updateUserList(UserVO userVO);
	int deleteUserList(String userId);
	UserVO selectUserPwByUserId(String userId);
	int updateUserPw(UserVO userVO);
}
