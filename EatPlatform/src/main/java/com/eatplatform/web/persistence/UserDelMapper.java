package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserDelVO;
import com.eatplatform.web.domain.UserVO;

@Mapper
public interface UserDelMapper {
	int insertUser(UserVO userVO);
	
	int permanentDeleteUserInfo(String deadline);
}
