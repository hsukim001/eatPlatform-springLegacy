package com.eatplatform.web.service;

import com.eatplatform.web.domain.StoreAddressVO;

public interface StoreAddressService {
	
	StoreAddressVO selectStoreAddressById(int storeId);
}
