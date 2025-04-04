package com.eatplatform.web.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatplatform.web.domain.HolidayVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.ReservWithStoreNameVO;
import com.eatplatform.web.persistence.HolidayMapper;
import com.eatplatform.web.persistence.ReservMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class HolidayServiceImple implements HolidayService{
	
	@Autowired
	private HolidayMapper holidayMapper;
	
	@Autowired
	private ReservMapper reservMapper;
		
	@Override
	public List<HolidayVO> searchHolidayList(int storeId) {
		return holidayMapper.selectHolidayListByStoreId(storeId);
	}
	
	@Override
	public int createdHoliday(HolidayVO holidayVO) {
		log.info("createdHoliday()");
		
		try {
			
			String currentHoliday = holidayVO.getHoliday();
			
			Date nowDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String nowDateFormat = formatter.format(nowDate);
			
			Date now = formatter.parse(nowDateFormat);
			Date holidayDateTime = formatter.parse(currentHoliday);
			
			// 함수명 (시간, 차이갑
			// 현재시간보다 이전의 시간 예외
			if (now.after(holidayDateTime)) {
				return 0;
			}
			
			HolidayVO vo = holidayMapper.selectHolidayByHolidayAndStoreId(holidayVO);
			if(vo != null) {
				return 0;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return holidayMapper.insertHoliday(holidayVO);
	}

	@Override
	public int registrationHolidayList(List<HolidayVO> holidayList) {
		log.info("createdHoliday()");
		int result = 0;
		int storeId = holidayList.get(0).getStoreId();
		List<HolidayVO> dbHolidayList = holidayMapper.selectHolidayListByHolidayAndStoreId(holidayList, storeId);
		
		try {
			if(dbHolidayList.size() > 0) {
				return result;
			}
			
			for(int i = 0; i < holidayList.size(); i++ ) {
				String currentHoliday = holidayList.get(i).getHoliday();
				
				Date nowDate = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String nowDateFormat = formatter.format(nowDate);
				
				Date now = formatter.parse(nowDateFormat);
				Date holidayDateTime = formatter.parse(currentHoliday);
				
				// 함수명 (시간, 차이갑
				// 현재시간보다 이전의 시간 예외
				if (now.after(holidayDateTime)) {
					return 0;
				}
			}
			
			int insertResult = holidayMapper.multipleInsertHoliday(holidayList);
			
			if(insertResult == holidayList.size()) {
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Transactional
	@Override
	public int deleteHoliday(List<HolidayVO> holidayList) {
		log.info("deleteHoliday()");
		
		HolidayVO holidayVO = new HolidayVO();
		
		for(int i = 0; i < holidayList.size(); i++) {
			holidayVO.setStoreId(holidayList.get(i).getStoreId());
			holidayVO.setHoliday(holidayList.get(i).getHoliday());
			holidayMapper.deleteHolidayByStoreIdHoliday(holidayVO);
		}
		
		return 1;
	}

	@Override
	public boolean isReservStatus(List<HolidayVO> holidayList, int storeId) {
		log.info("isReservStatus()");
		List<ReservWithStoreNameVO> reservDateList = reservMapper.selectReservDateByHolidayListStoreId(holidayList, storeId);
		
		for(int i = 0; i < holidayList.size(); i++) {
    		String holiday = holidayList.get(i).getHoliday();
    		for(int j = 0; j < reservDateList.size(); j++) {
    			if(holiday.equals(reservDateList.get(j).getReservDate())) {
    				return true;
    			}
    		}
    	}
		return false;
	}
	
}
