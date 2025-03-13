package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.StoreCategoryVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

public interface ManagementService {
	
	/**
	 * @param pagination
	 * @param username
	 * @return List<StoreVO>
	 */
	List<StoreVO> searchStoreList(Pagination pagination, String username);
	
	
	/**
	 * @param username
	 * @return int
	 */
	int getTotalStoresCount(String username);
	
}
