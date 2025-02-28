package com.eatplatform.web.service;

import java.util.ArrayList;
import java.util.List;

import com.eatplatform.web.domain.HolidayVO;
import com.eatplatform.web.domain.JoinReservUserNameVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.StoreScheduleVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

public interface ReservService {
	
	/**
	 * 예약 목록 조회
	 * @param pagination
	 * @return
	 */
	List<ReservVO> searchToDayList(Pagination pagination);
	
	/**
	 * 이전 예약 목록 조회
	 * @param pagination
	 * @return
	 */
	List<ReservVO> searchPrevDayList(Pagination pagination);
	
	/**
	 * 예약 가능 시간 조회
	 * @param storeScheduleVO
	 * @param personnel
	 * @return
	 */
	List<StoreScheduleVO> searchSchedule(StoreScheduleVO storeScheduleVO, int personnel);
	
	/**
	 * @param storeId
	 * @param reservDate
	 * @return List<StoreScheduleVO>
	 */
	List<StoreScheduleVO> searchReservList(int storeId, String reservDate);
	
	/**
	 * @param reservVO
	 * @return List<JoinReservUserNameVO>
	 */
	List<JoinReservUserNameVO> searchReservList(ReservVO reservVO);
	
	/**
	 * @param storeId
	 * @param reservDate
	 * @return List<ReservVO>
	 */
	List<ReservVO> searchReservListByHolidayList(List<HolidayVO> holidayList);
	
	/**
	 * 예약 목록 총 건수 조회
	 * @param userId
	 * @return
	 */
	int searchToDayTotalCount(int userId);

	/**
	 * 이전 예약 목록 총 건수 조회
	 * @param userId
	 * @return
	 */
	int searchPrevDayTotalCount(int userId);

	/**
	 * 예약 등록
	 * @param reservVO
	 * @param reservLimit
	 * @return
	 */
	int createdReserv(ReservVO reservVO, int reservLimit);

	/**
	 * 예약 취소
	 * @param reservId
	 * @return
	 */
	int cancelReserv(int reservId);
	
	/**
	 * @param List<ReservVO> cancelList
	 * @return int
	 */
	int cancelReservByList(List<ReservVO> cancelList);
}
