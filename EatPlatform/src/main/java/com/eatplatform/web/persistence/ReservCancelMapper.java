package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.domain.ReservVO;

@Mapper
public interface ReservCancelMapper {
	
	/**
	 * @param reservList
	 * @param requestType
	 * @return
	 */
	int insertReservCancelByReservList(@Param("reservList") List<ReservVO> reservList, @Param("requestType") String requestType);
	
	/**
	 * @param cancelList
	 * @param requestStatus
	 * @return
	 */
	int updateReservCancelAtRequestStatusByCancelId(@Param("cancelList") List<ReservCancelVO> cancelList, @Param("requestStatus") String requestStatus);
}
