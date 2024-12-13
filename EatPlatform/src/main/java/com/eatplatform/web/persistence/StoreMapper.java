package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.StoreVO;

@Mapper
public interface StoreMapper {
	int insertStore(StoreVO storeVO);
}
