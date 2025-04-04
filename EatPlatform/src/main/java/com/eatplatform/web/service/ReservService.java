package com.eatplatform.web.service;

import java.util.ArrayList;
import java.util.List;

import com.eatplatform.web.domain.HolidayVO;
import com.eatplatform.web.domain.JoinReservUserNameVO;
import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.domain.ReservInfoVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.CancelReservInfoVO;
import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.ReservWithStoreNameVO;
import com.eatplatform.web.domain.StoreScheduleVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

public interface ReservService {
	
	/**
	 * 예약 목록 조회
	 * @param pagination
	 * @return
	 */
	List<ReservWithStoreNameVO> getReservWithStoreNameList(Pagination pagination, String auth, String username);
	
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
	List<ReservWithStoreNameVO> searchReservListByHolidayList(List<HolidayVO> holidayList, int storeId);
	
	/**
	 * @param storeId
	 * @param reservStatus
	 * @return List<ReservWithReservCacnelVO>
	 */
	List<CancelReservInfoVO> getRequestCancelList(int storeId, int cancelStatus);
	
	/**
	 * @param reservId
	 * @return reservInfoVO
	 */
	ReservInfoVO getReservInfoByReservId(int reservId);
	
	/**
	 * 예약 목록 총 건수 조회
	 * @param userId
	 * @return
	 */
	int getTotalCount(int userId, String auth, String keyword, String username, String type);
	
	/**
	 * @param userId
	 * @return boolean
	 */
	boolean isReservUser(int reservId, int userId);

	/**
	 * 예약 등록
	 * @param reservVO
	 * @param reservLimit
	 * @return
	 */
	int createdReserv(ReservVO reservVO, int reservLimit);
	
	/**
	 * @param List<ReservVO> cancelList
	 * @return int
	 */
	int createdCancelHistory(List<ReservCancelVO> cancelList, String requestType, CustomUser customUser);
	
	/**
	 * @param cancelList
	 * @return int
	 */
	int updateCancelStatusByList(List<ReservCancelVO> cancelList);
	
}
