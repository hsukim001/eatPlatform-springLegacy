package com.eatplatform.web.persistence;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.StoreImageVO;

@Mapper
public interface StoreImageMapper {
	
	List<StoreImageVO> selectListByStoreId(int storeId);
	
	List<StoreImageVO> selectListByStoreImagePath(
			@Param("storeImageDate") String storeImageDate);
	
	StoreImageVO selectByStoreImageId(int storeImageId);
	
	int insertStoreImage(StoreImageVO storeImageVO);
	int deleteStoreImageByStoreId(int storeId);
	
}
