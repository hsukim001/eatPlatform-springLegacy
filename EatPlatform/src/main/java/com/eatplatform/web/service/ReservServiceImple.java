package com.eatplatform.web.service;

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
	
	@Override
	public List<ReservVO> searchList() {
		log.info("searchList");
		List<ReservVO> list = reservMapper.selectList();
		return list;
	}

	@Override
	public ReservVO searchByReservId(int reservId) {
		log.info("searchByReservId()");
		return reservMapper.selectByReservId(reservId);
	}

	@Override
	public List<ReservVO> searchByUserId(String userId) {
		log.info("searchByUserId()");
		List<ReservVO> list = reservMapper.selectByUserId(userId);
		return list;
	}

	@Override
	public int createdReserv(ReservVO reservVO) {
		log.info("insertReserv()");
		int result = reservMapper.insert(reservVO);
		return result;
	}

	@Override
	public int deleteReserv(int reservId) {
		log.info("deleteReserv()");
		int result = reservMapper.delete(reservId);
		return result;
	}
	
}
