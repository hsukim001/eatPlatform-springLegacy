package com.eatplatform.web.service;

import com.eatplatform.web.domain.UserVO;

public interface UserService {
	
	// 회원 가입
	int createdUser(UserVO userVO);
	// 회원 검색
	UserVO searchUser(String userId);
	// 회원 수정
	int modifyUser(UserVO userVO);
	// 로그인
	int login(String userId, String userPw);
	// 비밀번호 수정
	int modifyUserPw(UserVO userVO);
	// 사용자 확인
	int checkUserByUserId(String userId, String type);
	// 아이디 찾기
	String searchUserId(String userEmail);
	// 회원 삭제
	int deleteUser(char status, String userId);
	// 회원 목록 삭제
	int deleteUserList(char userActiveYn);
}
