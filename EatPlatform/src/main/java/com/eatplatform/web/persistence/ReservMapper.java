package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface ReservMapper {
	List<ReservVO> selectList();
	ReservVO selectByReservId(int reservId);
	List<ReservVO> selectByUserId(String userId);
	
	List<ReservVO> selectPagingToDay(Pagination pagination);
	List<ReservVO> selectPagingPrevDay(Pagination pagination);
	int selectToDayTotalCount(String userId);
	int selectPrevDayTotalCount(String userId);
	
	int insert(ReservVO reservVO);
	int delete(int reservId);
}
