package com.eatplatform.web.service;

import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.UserVO;

public interface UserService {
	// 회원 정보 검색(userId)
	UserVO searchUser(int userId);
	// 회원 아이디 찾기(email)
	String searchUsername(String email);
	// 회원 이름 조회(userId)
	String getStoreUserNameByUserId(@Param("userName") String userName);
	// 아이디 중복 확인(username)
	int checkUser(String username);
	// 회원 가입
	int createdUser(UserVO userMemberVO);
	// 회원 정보 수정
	int updateUser(UserVO userMemberVO);
	// 비밀번호 수정
	int modifyPassword(UserVO userMemberVO, String auth);
	// 회원 탈퇴
	int withdrawalUser(int userId, String auth);
	// 탈퇴 회원 정보 탈퇴 목록으로 이전
	int transferWithdrawalUser(int active);
	
}
