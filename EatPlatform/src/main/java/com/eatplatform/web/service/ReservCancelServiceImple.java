package com.eatplatform.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.ReservCancelVO;
import com.eatplatform.web.persistence.ReservCancelMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReservCancelServiceImple implements ReservCancelService{
	
	@Autowired
	private ReservCancelMapper reservCancelMapper;
	
	@Override
	public ReservCancelVO getReservCancel(int reservId) {
		return reservCancelMapper.selectReservCancelByReservId(reservId);
	}
}
