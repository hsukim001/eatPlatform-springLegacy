package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.StoreVO;

public interface StoreService {
	
	// 데이터 추가
	int registerStore(StoreVO storeVO);

	// pageNum과 pageSize를 받아 페이징 처리 결과를 리턴
	List<StoreVO>getStoresWithPaging(int pageNum, int pageSize);
	
	// 전체 게시글 카운트
	int getTotalStoresCount();
}