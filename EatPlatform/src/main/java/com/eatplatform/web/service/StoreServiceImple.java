package com.eatplatform.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.domain.StoreCategoryVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.persistence.ProductMapper;
import com.eatplatform.web.persistence.StoreAddressMapper;
import com.eatplatform.web.persistence.StoreApprovalsMapper;
import com.eatplatform.web.persistence.StoreMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class StoreServiceImple implements StoreService {

	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private StoreAddressMapper storeAddressMapper;
	
	@Autowired
	private StoreApprovalsMapper storeApprovalsMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Transactional(value = "transactionManager")
	@Override
	public int registerStore(StoreVO storeVO, StoreAddressVO storeAddressVO, StoreCategoryVO storeCategoryVO) {
		log.info("registerStore()");
		int resultStore = storeMapper.insertStore(storeVO);
		int storeId = storeVO.getStoreId();
		int mainCategoryId = storeCategoryVO.getMainCategoryId();
		int subCategoryId = storeCategoryVO.getSubCategoryId();
		String mainCategoryName = productMapper.selectMainCategoryNameByMainCategoryId(mainCategoryId);
		String subCategoryName = productMapper.selectSubCategoryNameBySubCategoryId(subCategoryId);
		storeCategoryVO.setStoreId(storeId);
		storeCategoryVO.setMainCategoryName(mainCategoryName);
		storeCategoryVO.setSubCategoryName(subCategoryName);
		boolean resultCategory = storeMapper.insertStoreCategory(storeCategoryVO);
		storeAddressVO.setStoreId(storeId);
		int resultAddress = storeAddressMapper.insertStoreAddress(storeAddressVO);
		StoreApprovalsVO storeApprovalsVO = new StoreApprovalsVO();
		storeApprovalsVO.setStoreId(storeId);
		storeApprovalsVO.setApprovals(0);
		int resultApprovals = storeApprovalsMapper.insertStoreApprovals(storeApprovalsVO);
		log.info(storeVO);
		log.info(storeAddressVO);
		log.info("사용자 정보 " + resultCategory + "행 삽입 성공");
		log.info("주소 정보 " + resultAddress + "행 삽입 성공");
		log.info("카테고리 정보 " + resultAddress + "행 삽입 성공");
		log.info("승인 요청 정보" + resultApprovals + "행 삽입 성공");
		return resultStore;
	}

	@Override
	public String getStoreUserIdByStoreId(int storeId) {
		return storeMapper.getStoreUserIdByStoreId(storeId);
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
	public int modifyStore(StoreVO storeVO, StoreAddressVO storeAddressVO) {
		log.info("modifyStore()");
		int resultStore = storeMapper.updateStore(storeVO);
		int resultAddress = storeAddressMapper.updateStoreAddress(storeAddressVO);
		log.info(storeVO);
		log.info(resultStore + "행 정보 수정 성공");
		log.info(resultAddress + "행 주소 정보 수정 성공");
		return resultStore;
	}

	@Override
	public List<StoreVO> getStoreIdByStoreUserId(String storeUserId) {
		return storeMapper.getStoreIdByStoreUserId(storeUserId);
	}

	@Override
	public String getStoreNameByStoreId(int storeId) {
		return storeMapper.getStoreNameByStoreId(storeId);
	}

}
