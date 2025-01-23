package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.StoreHolidaysVO;

@Mapper
public interface StoreHolidaysMapper {
	// 식당 휴무일 등록
	int insertStoreHolidays(StoreHolidaysVO storeHolidaysVO);
	// 식당 휴무일 삭제
	int deleteStoreHolidays(int storeHolidaysId);
	// 식당 휴무일 목록 조회(storeId)
	List<StoreHolidaysVO> selectStoreHolidaysList(int storeId);
}
