package com.eatplatform.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public String getUserIdByStoreId(int storeId) {
		return storeMapper.getUserIdByStoreId(storeId);
	}

	@Override
	public StoreVO selectStoreById(int storeId) {
		return storeMapper.selectStoreById(storeId);
	}
	
	public List<StoreVO> getStoresWithPaging(int pageNum, int pageSize, String keyword) {
	    int startRow = (pageNum - 1) * pageSize + 1; 
	    int endRow = pageNum * pageSize;

	    Map<String, Object> params = new HashMap<>();
	    params.put("startRow", startRow);
	    params.put("endRow", endRow);
	    params.put("keyword", keyword); 
	    log.info("Start Row: " + startRow + ", End Row: " + endRow + ", Keyword: " + keyword);	    
	    return storeMapper.getStoresWithPaging(params); 
	}

	public int getTotalStoresCount(String keyword) {
	    return storeMapper.getTotalStoresCount(keyword); 
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
