package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.BusinessRequestVO;

@Mapper
public interface BusinessRequestMapper {
	// 사업자 등록 신청
	int insertBusinessRequest(BusinessRequestVO businessRequestVO);
	// 사업자 등록 신청 상세
	BusinessRequestVO selectBusinessRequestByBusinessRequestId(int businessRequestId);
}
