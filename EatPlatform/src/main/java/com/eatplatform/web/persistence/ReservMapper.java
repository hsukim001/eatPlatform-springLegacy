package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.HolidayVO;
import com.eatplatform.web.domain.JoinReservUserNameVO;
import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.domain.ReservInfoVO;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.CancelReservInfoVO;
import com.eatplatform.web.domain.ReservWithStoreNameVO;
import com.eatplatform.web.domain.StoreScheduleVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface ReservMapper {
	
	/**
	 * @param Pagination pagination
	 * @param String keyword
	 * @return List<ReservWithStoreNameVO>
	 */
	List<ReservWithStoreNameVO> selectReservWithStoreNameListByPaginationAndKeywordAndUserId(@Param("pagination") Pagination pagination);
	
	/**
	 * @param pagination
	 * @param keyword
	 * @param storeList
	 * @return List<ReservWithStoreNameVO>
	 */
	List<ReservWithStoreNameVO> selectReservWithStoreNameListByPaginationAndKeywordAndStoreIdAndUserId(@Param("pagination") Pagination pagination, @Param("storeList") List<StoreVO> storeList);
	
	/**
	 * @param reservId
	 * @return
	 */
	ReservInfoVO joinReservWithStoreAndStoreAddressByReservInfo(int reservId);

	/**
	 * @param storeId
	 * @param reservDate
	 * @return List<StoreScheduleVO>
	 */
	List<StoreScheduleVO> selectStoreScheduleListByStoreIdReservDate(@Param("storeId") int storeId, @Param("reservDate") String reservDate);
	
	/**
	 * @param storeId
	 * @param reservDate
	 * @return List<ReservVO>
	 */
	List<ReservWithStoreNameVO> selectReservListByHolidayList(@Param("holidayList") List<HolidayVO> holidayList, @Param("storeId") int storeId);
	
	/**
	 * @param reservVO
	 * @return List<JoinReservUserNameVO>
	 */
	List<JoinReservUserNameVO> selectReservListByStoreIdReservDateReservHourReservMin(ReservVO reservVO);
	
	/**
	 * @param List<HolidayVO> holidayList
	 * @return List<ReservVO>
	 */
	List<ReservWithStoreNameVO> selectReservDateByHolidayListStoreId(@Param("holidayList") List<HolidayVO> holidayList, @Param("storeId") int storeId);
	
	/**
	 * @param storeId
	 * @param reservStatus
	 * @return List<ReservWithReservCancelVO>
	 */
	List<CancelReservInfoVO> joinReservWithCancelReservIdCancelCommentAndUserNamePhoneByStoreIdCancelStatus(@Param("storeId") int storeId, @Param("cancelStatus") int cancelStatus);
	
	// 예약 목록 총 건수 조회
	int selectTotalCountByUserId(@Param("userId") int userId, @Param("type") String type, @Param("keyword") String keyword);
	
	/**
	 * @param storeList
	 * @param keyword
	 * @return int
	 */
	int selectTotalCountByStoreId(@Param("storeList") List<StoreVO> storeList, @Param("type") String type, @Param("keyword") String keyword);
	
	// 이전 예약 목록 총 건수 조회
	int selectPrevDayTotalCount(int userId);
	
	/**
	 * @param reservId
	 * @param userId
	 * @return
	 */
	int checkReservByUserId(@Param("reservId") int reservId, @Param("userId") int userId);
	
	/**
	 * @param userId
	 * @return
	 */
	int selectReservCancelTotalCount(int userId);
	// 예약 등록
	int insert(ReservVO reservVO);
	/**
	 * 예약 취소 상태 변경
	 * @param cancelList
	 * @param cancelStatus
	 * @return
	 */
	int updateCancelStatus(@Param("cancelList") List<ReservCancelVO> cancelList, @Param("cancelStatus") int cancelStatus);
	// 예약 가능시간 조회
	List<StoreScheduleVO> selectSchedule(StoreScheduleVO storeScheduleVO);
	// 예약 가능 확인
	int selectTotalPersonnelByStoreIdDateHourMin(ReservVO reservVO);
	
	/**
	 * 예약 번호로 예약 조회
	 * @param reservId
	 * @return ReservVO
	 */
	ReservVO selectReservByReservId(int reservId);
	
}
