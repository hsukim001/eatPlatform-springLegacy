package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserDelVO;

@Mapper
public interface UserDelMapper {	
	int deleteWithdrawlUser(String deadline);
}
