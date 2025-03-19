package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.RoleListVO;

@Mapper
public interface RoleListMapper {
	RoleListVO selectUserRoleByUsername(String username);
		
	int insertUserRole(RoleListVO roleListVO);
	
	int updateUserRoleNameByUserName(RoleListVO roleListVO);
	
	int deleteWithdrowalUserRole();
	
	int deleteWithdrowalStoreUserRole();
	
	int deleteWithdrowalAdminUserRole();
	
}
