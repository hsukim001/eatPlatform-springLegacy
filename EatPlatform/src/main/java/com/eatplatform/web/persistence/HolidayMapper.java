package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.HolidayVO;

@Mapper
public interface HolidayMapper {
	
	/**
	 * @param holidayList
	 * @param storeId
	 * @return
	 */
	List<HolidayVO> selectHolidayListByHolidayAndStoreId(@Param("holidayList") List<HolidayVO> holidayList, @Param("storeId") int storeId);
	
	/**
	 * @param storeId
	 * @return List<HolidayVO>
	 */
	List<HolidayVO> selectHolidayListByStoreId(int storeId);
	
	/**
	 * @param holidayVO
	 * @return
	 */
	HolidayVO selectHolidayByHolidayAndStoreId(HolidayVO holidayVO);
	
	/**
	 * @param holidayVO
	 * @return int
	 */
	int insertHoliday(HolidayVO holidayVO);
	
	/**
	 * @param holidayList
	 * @return int
	 */
	int multipleInsertHoliday(@Param("holidayList") List<HolidayVO> holidayList);
	
	int deleteHolidayByStoreIdHoliday(HolidayVO holidayVO);
}
