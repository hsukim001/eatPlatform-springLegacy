package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.StoreImageVO;

public interface StoreImageService {
	
	StoreImageVO getStoreImageById(int storeImageId);
	List<StoreImageVO> getImageListByStoreId(int storeId);
	
}
