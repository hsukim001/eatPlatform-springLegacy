package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserRoleVO;

@Mapper
public interface UserRoleMapper {
	UserRoleVO selectUserRoleByUsername(String username);
		
	int insertUserRole(UserRoleVO roleListVO);
	
	int updateUserRoleNameByUserName(UserRoleVO userRoleVO);
	
	int deleteWithdrowalUserRole();
	
	int deleteWithdrowalStoreUserRole();
	
	int deleteWithdrowalAdminUserRole();
	
}
