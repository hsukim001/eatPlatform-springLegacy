package com.eatplatform.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.persistence.StoreMapper;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ManagementServiceImple implements ManagementService {
	
	@Autowired
	private StoreMapper storeMapper;
	
	/**
	 * @param Pagination
	 * @param username
	 * @return
	 */
	@Override
	public List<StoreVO> searchStoreList(Pagination pagination, String username) {
		return storeMapper.selectStoreListByStoreUserId(pagination, username);
	}

	/**
	 * @param String username
	 * @return int
	 */
	@Override
	public int getTotalStoresCount(String username) {
		return storeMapper.getTotalStoresCountByStoreUserId(username);
	}
	
}
