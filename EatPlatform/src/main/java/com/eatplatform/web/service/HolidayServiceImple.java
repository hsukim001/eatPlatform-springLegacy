package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.HolidayVO;
import com.eatplatform.web.persistence.HolidayMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class HolidayServiceImple implements HolidayService{
	
	@Autowired
	private HolidayMapper holidayMapper;
	
	@Override
	public int createdHoliday(HolidayVO holidayVO) {
		log.info("createdHoliday()");
		return holidayMapper.insertHoliday(holidayVO);
	}
	
}
