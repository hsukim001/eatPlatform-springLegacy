package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.persistence.StoreAddressMapper;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class StoreAddressServiceImple implements StoreAddressService {

	@Autowired
	private StoreAddressMapper storeAddressMapper;

	@Override
	public StoreAddressVO selectStoreAddressById(int storeId) {
		return storeAddressMapper.selectStoreAddressById(storeId);
	}


}
