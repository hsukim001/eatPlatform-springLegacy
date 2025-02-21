package com.eatplatform.web.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface StoreMapper {
	// 데이터 추가
	int insertStore(StoreVO storeVO);
	
	// 전체 게시물 조회
	List<StoreVO> getRecentStores(Map<String, Integer> params);
	
	List<StoreVO> selectStoreListByStoreUserId(@Param("pagination") Pagination pagination, @Param("storeUserId") String storeUserId);
	
	//  List Pagination
	List<StoreVO> getStoresWithPaging(Map<String, Object> params);

	// storeId로 검색하여 조회
	StoreVO selectStoreById(int storeId);
	
	// storeId로 userId 검색
	String getStoreUserIdByStoreId(@Param("storeId") int storeId);
	
	String getStoreNameByStoreId(@Param("storeId") int storeId);
	
	// storeUserId로 storeId 검색
	List<StoreVO> getStoreIdByStoreUserId(@Param("storeUserId") String storeUserId);

	int getTotalStoresCount(Map<String, Object> params);
	
	int getTotalStoresCountByStoreUserId(String storeUserId);

	int updateStore(StoreVO storeVO);
	
	int deleteStore(int storeid);
}
