package com.eatplatform.web.persistence;


import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserVO;

@Mapper
public interface UserAdminMapper {
	// 관리자 정보 검색(userId)
	UserVO selectUserByUserId(int userId);
	// 관리자 정보 검색(username)
	UserVO selectUserByUsername(String username);
	// 비활성화 관리자 정보 이전(active)
	int insertWithdrawlUserByActive(int active);
	// 관리자 정보 수정
	int updateUser(UserVO userMemberVO);
	// 비밀번호 변경
	int updatePassword(UserVO userMemberVO);
	// 관리자 활성화/비활성화(UserMemberVO)
	int updateUserActive(UserVO userMemberVO);
	// 사업자 회원 정보 삭제
	int deleteUserByActive(int active);
}
