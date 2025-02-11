package com.eatplatform.web.persistence;


import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.UserVO;

@Mapper
public interface UserStoreMapper {
	// 사업자 회원 정보 검색(userId)
	UserVO selectUserByUserId(int userId);
	// 사업자 회원 정보 검색(username)
	UserVO selectUserByUsername(String username);
	// 비활성화 사업자 회원 정보 이전(active)
	int insertWithdrawlUserByActive(int active);
	// 일반회원 테이블 데이터를 가져와 사업자회원 테이블에 삽입
	int insertUserStoreFromUser(int userId);
	// 사업자 회원 정보 수정
	int updateUser(UserVO userMemberVO);
	// 비밀번호 변경
	int updatePassword(UserVO userMemberVO);
	// 사업자 회원 활성화/비활성화
	int updateUserActive(UserVO userMemeberVO);
	// 사업자 회원 정보 삭제
	int deleteUserByActive(int active);
}
