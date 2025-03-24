package com.eatplatform.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.StoreAddressVO;
import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.domain.StoreCategoryVO;
import com.eatplatform.web.domain.StoreImageVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.persistence.ProductMapper;
import com.eatplatform.web.persistence.StoreAddressMapper;
import com.eatplatform.web.persistence.StoreApprovalsMapper;
import com.eatplatform.web.persistence.StoreImageMapper;
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
	
	@Autowired
	private StoreImageMapper storeImageMapper;
	
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
		
		// 이미지
		List<StoreImageVO> storeImageList = storeVO.getStoreImageList();
		
		for (StoreImageVO storeImageVO : storeImageList) {
			storeImageVO.setStoreId(storeId);
			storeImageMapper.insertStoreImage(storeImageVO);
			log.info("이미지 등록 : " + storeImageVO);
		}
		
		return resultStore;
	}

	@Override
	public String getStoreUserIdByStoreId(int storeId) {
		return storeMapper.getStoreUserIdByStoreId(storeId);
	}

	@Override
	public StoreVO selectStoreById(int storeId) {
		StoreVO storeVO = storeMapper.selectStoreById(storeId);
		List<StoreImageVO> storeImageList = storeImageMapper.selectListByStoreId(storeId);
		storeVO.setStoreImageList(storeImageList);
		
		return storeVO;
	}
	
	@Override
	public StoreCategoryVO getStoreCategoryByStoreId(int storeId) {
		return storeMapper.selectStoreCategoryByStoreId(storeId);
	}
	
	// StoreCategory 조회
	@Override
	public List<StoreCategoryVO> getStoreCategory(@Param("storeIdList") List<Integer> storeIdList) {
		return storeMapper.selectStoreCategory(storeIdList);
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
	public int modifyStore(StoreVO storeVO, StoreAddressVO storeAddressVO, StoreCategoryVO storeCategoryVO) {
		log.info("modifyStore()");
		int resultStore = storeMapper.updateStore(storeVO);
		int resultAddress = storeAddressMapper.updateStoreAddress(storeAddressVO);
		int mainCategoryId = storeCategoryVO.getMainCategoryId();
		int subCategoryId = storeCategoryVO.getSubCategoryId();
		String mainCategoryName = productMapper.selectMainCategoryNameByMainCategoryId(mainCategoryId);
		String subCategoryName = productMapper.selectSubCategoryNameBySubCategoryId(subCategoryId);

		storeCategoryVO.setMainCategoryName(mainCategoryName);
		storeCategoryVO.setSubCategoryName(subCategoryName);
		int resultStoreCategory = storeMapper.updateStoreCategory(storeCategoryVO);
		
		// 이미지 수정
		List<StoreImageVO> storeImageList = storeVO.getStoreImageList();
		
		if(storeImageList.isEmpty()) {
			storeImageList = storeVO.getStoreImageList();
		} else {
			storeImageMapper.deleteStoreImageByStoreId(storeVO.getStoreId());
			
			for(StoreImageVO storeImageVO : storeImageList) {
				storeImageVO.setStoreId(storeVO.getStoreId());
				storeImageMapper.insertStoreImage(storeImageVO);
			}
		}
		
		log.info(storeVO);
		log.info(resultStore + "행 정보 수정 성공");
		log.info(resultAddress + "행 주소 정보 수정 성공");
		log.info(storeImageList + "이미지 수정 성공"); 
		log.info(resultStoreCategory + "행 카테고리 정보 수정 성공");
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

	@Transactional(value = "transactionManager")
	@Override
	public int deleteStore(int storeId) {
		int result = storeMapper.deleteStore(storeId);
		storeImageMapper.deleteStoreImageByStoreId(storeId);
		
		return result;
	}



}
