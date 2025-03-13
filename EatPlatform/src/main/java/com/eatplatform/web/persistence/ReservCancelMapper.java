package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.domain.ReservVO;

@Mapper
public interface ReservCancelMapper {
	
	/**
	 * @param int reservId
	 * @return ReservCancelVO
	 */
	ReservCancelVO selectReservCancelByReservId(int reservId);
		
	/**
	 * @param reservList
	 * @param requestType
	 * @return
	 */
	int insertReservCancelByReservList(@Param("cancelList") List<ReservCancelVO> cancelList, @Param("requestType") String requestType);
	
}
