package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.JoinBusinessRequestVO;
import com.eatplatform.web.domain.JoinStoreApprovalsInfoVO;
import com.eatplatform.web.domain.JoinStoreApprovalsListVO;
import com.eatplatform.web.domain.JoinStoreWithStoreApprovalsVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

public interface StoreApprovalsService {
	// 가게 등록 요청 총 건수
	int getTotalCount();
	// 가게 등록 요청 목록 조회
	List<JoinStoreApprovalsListVO> searchList(Pagination pagination);
	
	/**
	 * @param String username
	 * @return List<JoinStoreWithStoreApprovalsVO>
	 */
	List<JoinStoreWithStoreApprovalsVO> getStoreApprovals(Pagination pagination, String username);
	// 가게 등록 요청 정보
	JoinStoreApprovalsInfoVO searchInfo(int storeId);
	// 가게 등록 요청 승인
	int storeApproval(StoreApprovalsVO storeApprovalsVO);
	// 가게 등록 요청 거부, 취소
	int denialManagement(int storeId);
	
	StoreApprovalsVO getApprovalsByStoreId(int storeId);
}
