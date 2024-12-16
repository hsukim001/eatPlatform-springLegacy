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


	public List<StoreVO> getStoresWithPaging(int pageNum, int pageSize) {
	    int startRow = (pageNum - 1) * pageSize + 1; 
	    int endRow = pageNum * pageSize;

	    Map<String, Integer> params = new HashMap<>();
	    params.put("startRow", startRow);
	    params.put("endRow", endRow);
	    log.info("Start Row: " + startRow + ", End Row: " + endRow);
	    return storeMapper.getStoresWithPaging(params); 
	}

	public int getTotalStoresCount() {
	    return storeMapper.getTotalStoresCount(); 
	}

}
