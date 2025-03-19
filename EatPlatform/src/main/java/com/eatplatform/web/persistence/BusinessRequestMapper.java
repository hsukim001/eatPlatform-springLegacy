package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.JoinBusinessRequestWithUserAndRoleListVO;
import com.eatplatform.web.domain.BusinessRequestVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface BusinessRequestMapper {
	// 사업자 등록 요청 정보 조회
	BusinessRequestVO selectBusinessRequest(int businessRequestId);
	
	/**
	 * @param businessRequestId
	 * @return JoinBusinessRequestWithUserAndRoleListVO
	 */
	JoinBusinessRequestWithUserAndRoleListVO joinBusinessRequestWithUserMemberAndRoleListByBusinessRequestId(int businessRequestId);
	// 사업자 등록 신청
	int insertBusinessRequest(int userId);
	// 사업자 등록 신청 목록
	List<JoinBusinessRequestVO> selectBusinessRequestListByPagination(Pagination pagination);
	// 사업자 등록 신청 목록 총 건수
	int selectTotalCount();
	// 사업자 등록 신청 상세
	JoinBusinessRequestVO selectBusinessRequestByBusinessRequestId(int businessRequestId);
	// 사업자 등록 신청 조회(userId)
	int selectBusinessRequestIdByuserId(int userId);
	
	/**
	 * @param businessRequestId
	 * @param requestStatus
	 * @return int
	 */
	int updateRequestStatusByBusinessRequestId(@Param("businessRequestId") int businessRequestId, @Param("requestStatus") String requestStatus);
	
	// 사업자 등록 요청 정보 삭제
	int deleteBusinessRequest(int businessRequestId);
}
