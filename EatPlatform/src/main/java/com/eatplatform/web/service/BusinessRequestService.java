package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.util.Pagination;

public interface BusinessRequestService {
	
	// 사업자 등록 신청 목록
	List<JoinBusinessRequestVO> searchBusinessRequestList(Pagination pagination);
	// 사업자 등록 신청 상세 정보 조회
	JoinBusinessRequestVO searchBusinessRequestInfo(int businessRequestId);
	// 사업자 등록 신청
	int businessRequest(int userId);
	// 사업자 등록 신청 목록 총 건수
	int getBusinessRequestTotalCount();
	// 사업자 등록 신청 조회(userId)
	int getBusinessRequestId(int userId);
	
	/**
	 * 사업자 등록 요청 승인
	 * @param businessRequestId
	 * @param requestStatus
	 * @return int
	 */
	int businessRequestApploval(int businessRequestId);
	
	/**
	 * 사업자 등록 요청 상태 변경
	 * @param businessRequestId
	 * @param requestStatus
	 * @return int
	 */
	int updateRequestStatus(int businessRequestId, String requestStatus);
	
	/**
	 * 사업자 등록 요청 취소
	 * @param businessRequestId
	 * @return int
	 */
	int businessRequestCancel(int businessRequestId);
	
	/**
	 * 사업자 요청 회원 MEMBER 권한 확인
	 * @param businessRequestId
	 * @return
	 */
	boolean isBusinessRequestRoleMemberAndRequestStatusWait(int businessRequestId);
}
