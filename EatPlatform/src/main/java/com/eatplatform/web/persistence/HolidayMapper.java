package com.eatplatform.web.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.HolidayVO;

@Mapper
public interface HolidayMapper {
	
	/**
	 * @param holidayVO
	 * @return int
	 */
	int insertHoliday(HolidayVO holidayVO);
	
}
