package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.BusinessRequestInfoVO;
import com.eatplatform.web.domain.BusinessRequestVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface BusinessRequestMapper {
	// 사업자 등록 요청 정보 조회
	BusinessRequestVO selectBusinessRequest(int businessRequestId);
	// 사업자 등록 신청
	int insertBusinessRequest(BusinessRequestVO businessRequestVO);
	// 사업자 등록 신청 목록
	List<BusinessRequestInfoVO> selectBusinessRequestListByPagination(Pagination pagination);
	// 사업자 등록 신청 목록 총 건수
	int selectTotalCount();
	// 사업자 등록 신청 상세
	BusinessRequestInfoVO selectBusinessRequestByBusinessRequestId(int businessRequestId);
	// 사업자 등록 신청 조회(userId)
	int selectBusinessRequestIdByuserId(String userId);
	// 사업자 등록 요청 정보 삭제
	int deleteBusinessRequestByBusinessRequestId(int businessRequestId);
}
