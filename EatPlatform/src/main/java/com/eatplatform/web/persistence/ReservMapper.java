package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.StoreScheduleVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface ReservMapper {
	// 예약 목록 조회
	List<ReservVO> selectPagingToDay(Pagination pagination);
	// 이전 예약 목록 조회
	List<ReservVO> selectPagingPrevDay(Pagination pagination);
	// 예약 목록 총 건수 조회
	int selectToDayTotalCount(String userId);
	// 이전 예약 목록 총 건수 조회
	int selectPrevDayTotalCount(String userId);
	// 예약 등록
	int insert(ReservVO reservVO);
	// 예약 삭제
	int delete(int reservId);
	// 예약 가능시간 조회
	List<StoreScheduleVO> selectSchedule(StoreScheduleVO storeScheduleVO);
	// 예약 가능 확인
	List<ReservVO> selectScheduleForUpdate(@Param("vo") ReservVO reservVO, @Param("reservLimit") int reservLimit);
}
