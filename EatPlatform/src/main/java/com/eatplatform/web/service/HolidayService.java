package com.eatplatform.web.service;

import com.eatplatform.web.domain.HolidayVO;

public interface HolidayService {
	
	/**
	 * @param holidayVO
	 * @return int
	 */
	int createdHoliday(HolidayVO holidayVO);
	
}
