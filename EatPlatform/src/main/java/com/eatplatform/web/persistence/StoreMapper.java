package com.eatplatform.web.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.StoreCategoryVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface StoreMapper {
	// 식당 데이터 등록
	int insertStore(StoreVO storeVO);
	
	// 식당 카테고리 등록
	boolean insertStoreCategory(StoreCategoryVO storeCategoryVO);
	
	// 전체 게시물 조회
	List<StoreVO> getRecentStores(Map<String, Integer> params);
	
	List<StoreVO> selectStoreListByStoreUserId(@Param("pagination") Pagination pagination, @Param("storeUserId") String storeUserId);
	
	//  List Pagination
	List<StoreVO> getStoresWithPaging(Map<String, Object> params);

	// storeId로 검색하여 조회
	StoreVO selectStoreById(int storeId);
	
	// StoreCategory 조회 (ByStoreId)
	StoreCategoryVO selectStoreCategoryByStoreId(@Param("storeId") int storeId);
	
	// StoreCategory 조회
	List<StoreCategoryVO> selectStoreCategory(@Param("storeIdList") List<Integer> storeIdList);
	
	// storeId로 userId 검색
	String getStoreUserIdByStoreId(@Param("storeId") int storeId);
	
	String getStoreNameByStoreId(@Param("storeId") int storeId);
	
	// storeUserId로 storeId 검색
	List<StoreVO> getStoreIdByStoreUserId(@Param("storeUserId") String storeUserId);

	int getTotalStoresCount(Map<String, Object> params);
	
	int getTotalStoresCountByStoreUserId(String storeUserId);

	int updateStore(StoreVO storeVO);
	
	int updateStoreCategory(StoreCategoryVO storeCategoryVO);
	
	int deleteStore(int storeid);
}
