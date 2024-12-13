package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReservVO;

public interface ReservService {
	List<ReservVO> searchList();
	ReservVO searchByReservId(int reservId);
	List<ReservVO> searchByUserId(String userId);
	
	int createdReserv(ReservVO reservVO);
	int deleteReserv(int reservId);
}
