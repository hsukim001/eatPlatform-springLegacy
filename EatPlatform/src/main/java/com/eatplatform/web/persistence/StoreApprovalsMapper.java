package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.StoreApprovalsVO;

@Mapper
public interface StoreApprovalsMapper {
	// 가게 등록 요청
	int insertStoreApprovals(StoreApprovalsVO storeApprovalsVO);
	// 가게 등록 요청 상세
	StoreApprovalsVO selectStoreApprovalsByStoreId(int storeId);
	// 식당 등록 요청 승인
	int updateStoreApprovals(StoreApprovalsVO storeApprovalsVO);
}
