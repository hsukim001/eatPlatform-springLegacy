package com.eatplatform.web.service;

import java.util.ArrayList;
import java.util.List;

import com.eatplatform.web.domain.JoinReservUserNameVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.StoreScheduleVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

public interface ReservService {
	// 예약 목록 조회
	List<ReservVO> searchToDayList(Pagination pagination);
	// 이전 예약 목록 조회
	List<ReservVO> searchPrevDayList(Pagination pagination);
	// 예약 목록 총 건수 조회
	int searchToDayTotalCount(int userId);
	// 이전 예약 목록 총 건수 조회
	int searchPrevDayTotalCount(int userId);
	// 예약 등록
	int createdReserv(ReservVO reservVO, int reservLimit);
	// 예약 취소
	int cancelReserv(int reservId);
	// 예약 가능 시간 조회
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
}
