package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface StoreApprovalsMapper {
	// 가게 등록 요청 총 건수
	int selectTotalCount();
	// 가게 등록 요청
	int insertStoreApprovals(StoreApprovalsVO storeApprovalsVO);
	// 가게 등록 요청 목록
	List<JoinBusinessRequestVO> selectApprovalsList(Pagination pagination);
	// 가게 등록 요청 상세
	JoinBusinessRequestVO selectApprovals(int storeId);
	// 식당 등록 요청 승인
	int updateStoreApprovals(StoreApprovalsVO storeApprovalsVO);
}
