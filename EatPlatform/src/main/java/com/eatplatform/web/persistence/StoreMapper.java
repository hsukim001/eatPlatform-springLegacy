package com.eatplatform.web.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.StoreVO;

@Mapper
public interface StoreMapper {
	// 데이터 추가
	int insertStore(StoreVO storeVO);
	
	// 전체 게시물 조회
	List<StoreVO> getRecentStores(Map<String, Integer> params);
	
	// storeId로 검색하여 조회
	StoreVO selectStoreById(int storeId);


	List<StoreVO> getStoresWithPaging(Map<String, Integer> params);
	
	int getTotalStoresCount();

}
