package com.eatplatform.web.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.StoreImageVO;
import com.eatplatform.web.persistence.StoreImageMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class StoreImageServiceImple implements StoreImageService{
	
	@Autowired
	private StoreImageMapper storeImageMapper;

	@Override
	public StoreImageVO getStoreImageById(int storeImageId) {
		return storeImageMapper.selectByStoreImageId(storeImageId);
	}

	@Override
	public List<StoreImageVO> getImageListByStoreId(int storeId) {
		return storeImageMapper.selectListByStoreId(storeId);
	}

	@Override
	public StoreImageVO getFirstStoreImage(int storeId) {
		
		List<StoreImageVO> images = storeImageMapper.selectListByStoreId(storeId);
		if (!images.isEmpty()) {
			return images.get(0); // 첫 번째 이미지 반환
		}
		return null;
	}


}
