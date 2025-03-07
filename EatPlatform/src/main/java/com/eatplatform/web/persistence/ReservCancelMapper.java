package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ReservVO;

@Mapper
public interface ReservCancelMapper {
	int insertReservCancelByReservList(@Param("reservList") List<ReservVO> reservList, @Param("requestType") String requestType);
}
