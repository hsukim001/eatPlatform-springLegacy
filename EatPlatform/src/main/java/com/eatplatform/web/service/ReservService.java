package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.util.Pagination;

public interface ReservService {
	List<ReservVO> searchList();
	ReservVO searchByReservId(int reservId);
	List<ReservVO> searchByUserId(String userId);
	
	List<ReservVO> searchPagingToDay(Pagination pagination);
	List<ReservVO> searchPagingPrevDay(Pagination pagination);
	
	int searchToDayTotalCountByReservDateUserId(String userId);
	int searchPrevDayTotalCountByReservDateUserId(String userId);
	
	int createdReserv(ReservVO reservVO);
	int deleteReserv(int reservId);
}
