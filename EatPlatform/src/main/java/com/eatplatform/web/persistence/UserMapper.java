package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserVO;

@Mapper
public interface UserMapper {
	UserVO selectUserByUserId(String userId);
	int insertUser(UserVO userVO);
	int updateUser(UserVO userVO);
	int deleteUser(String userId);
	int updateUserPwByUserId(UserVO userVO);
	int updateUserPwByUserEmail(UserVO userVO);
	int checkUserByUserId(UserVO userVO);
	int checkUserByUserEmail(String userEmail);
	int checkUserByUserIdUserEmail(UserVO userVO);
	UserVO selectUserIdByUserEmail(String userEmail);
	int updateActiveYnByUserId(UserVO userVO);
	List<UserVO> selectUserListByUserActiveYn(char userActiveYn);
}
