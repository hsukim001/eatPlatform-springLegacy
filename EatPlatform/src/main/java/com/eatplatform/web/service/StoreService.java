package com.eatplatform.web.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreCategoryVO;
import com.eatplatform.web.domain.StoreVO;

public interface StoreService {
	
	// 데이터 추가
	int registerStore(StoreVO storeVO, StoreAddressVO storeAddressVO, StoreCategoryVO stroeCategoryVO);

	// pageNum과 pageSize를 받아 페이징 처리 결과를 리턴
	List<StoreVO>getStoresWithPaging(int pageNum, int pageSize, List<String> keywords);

	// storeId로 검색하여 조회
	StoreVO selectStoreById(int storeId);
	
	// Store Category 조회 (ByStoreId)
	StoreCategoryVO getStoreCategoryByStoreId(@Param("storeId") int storeId);
	
	// StoreCategory 조회
	List<StoreCategoryVO> getStoreCategory(@Param("storeIdList") List<Integer> storeIdList);
	
	// storeId로 userId 검색
	String getStoreUserIdByStoreId(@Param("storeId") int storeId);
	
	//
	String getStoreNameByStoreId(@Param("storeId") int storeId);
	
	// storeUserId로 storeId 검색
	List<StoreVO> getStoreIdByStoreUserId(@Param("storeUserId") String storeUserId);
	
	/**
	 * 가게 별점순 정렬
	 * @return list
	 */
	List<StoreVO> getStoresByScore();
	
	// 전체 게시글 카운트
	int getTotalStoresCount(List<String> keywords);
	
	int modifyStore(StoreVO storeVO, StoreAddressVO storeAddressVO, StoreCategoryVO storeCategoryVO);
	
	/**
	 * 매장 삭제
	 * @param int storeId
	 * @return int
	 */
	int deleteStore(int storeId);

}
