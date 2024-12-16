package com.eatplatform.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.persistence.ReservMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReservServiceImple implements ReservService{
	
	@Autowired
	private ReservMapper reservMapper;
	
	// 예약 목록 조회
	@Override
	public List<ReservVO> searchList() {
		log.info("searchList");
		List<ReservVO> list = reservMapper.selectList();
		return list;
	}

	// 예약 상세 조회(reservId)
	@Override
	public ReservVO searchByReservId(int reservId) {
		log.info("searchByReservId()");
		return reservMapper.selectByReservId(reservId);
	}

	// 예약 목록 조회(userId)
	@Override
	public List<ReservVO> searchByUserId(String userId) {
		log.info("searchByUserId()");
		List<ReservVO> list = reservMapper.selectByUserId(userId);
		return list;
	}

	// 예약 등록
	@Override
	public int createdReserv(ReservVO reservVO) {
		log.info("insertReserv()");
		log.info("예약 등록일 : " + reservVO.getReservDate());
		log.info("예약 등록 시간 : " + reservVO.getReservTime());
		int result = reservMapper.insert(reservVO);
		return result;
	}

	// 예약 취소
	@Override
	public int deleteReserv(int reservId) {
		log.info("deleteReserv()");
		int result = reservMapper.delete(reservId);
		return result;
	}

	// 예약 목록 조회(reservDate, userId)
	@Override
	public List<ReservVO> searchToDayByReservDateUserId(String userId) {
		log.info("searchToDayByReservDateUserId()");
		List<ReservVO> list = new ArrayList<ReservVO>();
		list = reservMapper.selectToDayByReservDateUserId(userId);
		return list;
	}

	// 지난 예약 목록 조회(reservDate, userId)
	@Override
	public List<ReservVO> searchPrevDayByReservDateUserId(String userId) {
		log.info(userId);
		List<ReservVO> list = new ArrayList<ReservVO>();
		list = reservMapper.selectPrevDayByReservDateUserId(userId);
		return list;
	}
	
}
