package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserDelVO;

@Mapper
public interface WithdrawlUserMapper {	
	int deleteWithdrawlUser(String deadline);
}
