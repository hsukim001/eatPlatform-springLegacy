package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.RequestInfoVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.util.Pagination;

public interface StoreApprovalsService {
	// 가게 등록 요청 총 건수
	int getTotalCount();
	// 가게 등록 요청 목록 조회
	List<RequestInfoVO> searchList(Pagination pagination);
	// 가게 등록 요청 정보
	RequestInfoVO searchInfo(int storeId);
	// 가게 등록 요청 승인
	int storeApproval(StoreApprovalsVO storeApprovalsVO);
	// 가게 등록 요청 거부, 취소
	int denialManagement(int storeId);
}
