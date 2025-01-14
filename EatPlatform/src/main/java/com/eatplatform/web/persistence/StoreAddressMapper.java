package com.eatplatform.web.persistence;


import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreVO;

@Mapper
public interface StoreAddressMapper {
	// 데이터 추가.
	int insertStoreAddress(StoreAddressVO storeAddressVO);
	
	StoreAddressVO selectStoreAddressById(int storeId);
	
	int updateStoreAddress(StoreAddressVO storeAddressVO);
}
