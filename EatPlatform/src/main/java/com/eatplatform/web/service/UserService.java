package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.domain.UserVO;
import com.eatplatform.web.util.Pagination;

public interface UserService {
	// 회원 정보 검색(userId)
	UserVO searchUser(int userId);
	// 회원 아이디 찾기(email)
	String searchUsername(String email);
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
	
	// 사업자 등록 신청
	int businessRequest(StoreVO storeVO, StoreAddressVO storeAddressVO, int userId);
	// 사업자 등록 신청 목록
	List<JoinBusinessRequestVO> searchBusinessRequestList(Pagination pagination);
	// 사업자 등록 신청 상세 정보 조회
	JoinBusinessRequestVO searchBusinessRequestInfo(int businessRequestId);
	// 사업자 등록 신청 목록 총 건수
	int getBusinessRequestTotalCount();
	// 사업자 등록 신청 조회(userId)
	int getBusinessRequestId(int userId);
	// 사업자 등록 요청 승인
	int businessReqeustApprovals(int businessRequestId, int storeId);
	// 사업자 등록 요청 거부 관리
	int businessReqeustDenialManagement(int businessRequestId, int storeId);
}
