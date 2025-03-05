package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.HolidayVO;

public interface HolidayService {
	
	
	/**
	 * @param storeId
	 * @return List<HolidayVO>
	 */
	List<HolidayVO> searchHolidayList(int storeId);
	
	/**
	 * @param holidayVO
	 * @return int
	 */
	int createdHoliday(HolidayVO holidayVO);
	
	int registrationHolidayList(List<HolidayVO> holidayList);
	
	int deleteHoliday(List<HolidayVO> holidayList);
	
	boolean isReservStatus(List<HolidayVO> holidayList, int storeId);
}
