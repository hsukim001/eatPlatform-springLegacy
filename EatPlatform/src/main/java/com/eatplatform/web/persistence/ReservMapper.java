package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.ReservVO;

@Mapper
public interface ReservMapper {
	List<ReservVO> selectList();
	ReservVO selectByReservId(int reservId);
	List<ReservVO> selectByUserId(String userId);
	List<ReservVO> selectToDayByReservDateUserId(String userId);
	List<ReservVO> selectPrevDayByReservDateUserId(String userId);
	
	int insert(ReservVO reservVO);
	int delete(int reservId);
}
