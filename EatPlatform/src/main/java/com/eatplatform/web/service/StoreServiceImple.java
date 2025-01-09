package com.eatplatform.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.persistence.StoreAddressMapper;
import com.eatplatform.web.persistence.StoreMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class StoreServiceImple implements StoreService {

	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private StoreAddressMapper storeAddressMapper;
	
	@Transactional(value = "transactionManager")
	@Override
	public int registerStore(StoreVO storeVO, StoreAddressVO storeAddressVO) {
		log.info("registerStore()");
		int resultStore = storeMapper.insertStore(storeVO);
		int storeId = storeVO.getStoreId();
		storeAddressVO.setStoreId(storeId);
		int resultAddress = storeAddressMapper.insertStoreAddress(storeAddressVO);
		log.info(storeVO);
		log.info(storeAddressVO);
		log.info("사용자 정보 " + resultStore + "행 삽입 성공");
		log.info("주소 정보 " + resultAddress + "행 삽입 성공");
		return resultStore;
	}

	@Override
	public String getUserIdByStoreId(int storeId) {
		return storeMapper.getUserIdByStoreId(storeId);
	}

	@Override
	public StoreVO selectStoreById(int storeId) {
		return storeMapper.selectStoreById(storeId);
	}
	
	public List<StoreVO> getStoresWithPaging(int pageNum, int pageSize, List<String> keywords) {
	    int startRow = (pageNum - 1) * pageSize + 1; 
	    int endRow = pageNum * pageSize;

	    Map<String, Object> params = new HashMap<>();
	    params.put("startRow", startRow);
	    params.put("endRow", endRow);
	    params.put("keywords", keywords); 
	    log.info("Start Row: " + startRow + ", End Row: " + endRow + ", Keyword: " + keywords);	    
	    log.info(params);
	    return storeMapper.getStoresWithPaging(params); 
	}

	public int getTotalStoresCount(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            keywords = new ArrayList<>();
        }
	    Map<String, Object> params = new HashMap<>();
	    params.put("keywords", keywords); 
	    return storeMapper.getTotalStoresCount(params); 
	}


	@Override
	public int modifyStore(StoreVO storeVO) {
		log.info("modifyStore()");
		int result = storeMapper.updateStore(storeVO);
		log.info(storeVO);
		log.info(result + "행 수정성공");
		return result;
	}





}
