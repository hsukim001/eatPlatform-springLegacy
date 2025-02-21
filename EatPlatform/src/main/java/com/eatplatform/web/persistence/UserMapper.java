package com.eatplatform.web.persistence;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.JoinUserVO;
import com.eatplatform.web.domain.UserVO;

@Mapper
public interface UserMapper {
	// 회원 정보 검색(email)
	UserVO selectUserAllByEmail(String email);
	// 회원 정보 검색(username)
	UserVO selectUserByUsername(String username);
	// 회원 정보 검색(userId)
	UserVO selectUserByUserId(int userId);
	// 회원 아이디 찾기(email)
	UserVO selectUsernameByEmail(String email);
	// 회원 정보 및 권한 조회(username)
	JoinUserVO selectUserJoinUserRole(int userId);
	// 비활성화 회원 정보 이전(active)
	int insertWithdrawlUserByActive(int active);
	// 회원 활성화/비활성화(UserMemberVO)
	int updateUserActive(UserVO userVO);
	// 사용자 명 총 건수 조회(username)
	int countUser(String username);
	// 이메일 총 건 수 조회 (email)
	int countUserEmail(String email);
	// 이메일 총 건 수 조회(userMemberVO)
	int countUserEmailByUserIdEmail(UserVO userVO);
	// 회원 가입
	int insertUser(UserVO userVO);
	// 회원 정보 수정
	int updateUser(UserVO userVO);
	// 비밀번호 변경
	int updatePassword(UserVO userVO);
	// 회원 정보 삭제(active)
	int deleteUserByActive(int active);
	// 회원 정보 삭제(userId)
	int deleteUserByUserId(int userId);

	String selectStoreUserNameByUserId(@Param("userName") String userName);
}
