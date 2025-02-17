package com.eatplatform.web.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreVO;

public interface StoreService {
	
	// 데이터 추가
	int registerStore(StoreVO storeVO, StoreAddressVO storeAddressVO);

	// pageNum과 pageSize를 받아 페이징 처리 결과를 리턴
	List<StoreVO>getStoresWithPaging(int pageNum, int pageSize, List<String> keywords);

	// storeId로 검색하여 조회
	StoreVO selectStoreById(int storeId);
	
	// storeId로 userId 검색
	String getStoreUserIdByStoreId(@Param("storeId") int storeId);
	
	// 전체 게시글 카운트
	int getTotalStoresCount(List<String> keywords);
	
	int modifyStore(StoreVO storeVO, StoreAddressVO storeAddressVO);

}
