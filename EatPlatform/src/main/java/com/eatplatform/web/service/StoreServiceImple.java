package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.persistence.StoreMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class StoreServiceImple implements StoreService {

	@Autowired
	private StoreMapper storeMapper;
	
	@Override
	public int registerStore(StoreVO storeVO) {
		log.info("registerStore()");
		int result = storeMapper.insertStore(storeVO);
		log.info(storeVO);
		log.info(result + "행 삽입 성공");
		return result;
	}

}
