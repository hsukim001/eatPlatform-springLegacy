package com.eatplatform.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.StoreScheduleVO;
import com.eatplatform.web.domain.StoreVO;
import com.eatplatform.web.persistence.ReservMapper;
import com.eatplatform.web.persistence.StoreMapper;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReservServiceImple implements ReservService{
	
	@Autowired
	private ReservMapper reservMapper;
	
	@Autowired
	private StoreMapper storeMapper;
	
	// 페이징 예약 목록 조회
	@Override
	public List<ReservVO> searchToDayList(Pagination pagination) {
		log.info("searchPagingToDayByReservDateUserId()");
		return reservMapper.selectPagingToDay(pagination);
	}

	// 페이징 이전 예약 목록 조회
	@Override
	public List<ReservVO> searchPrevDayList(Pagination pagination) {
		log.info("searchPagingPrevDayByReservDateUserId()");
		return reservMapper.selectPagingPrevDay(pagination);
	}

	// 예약 목록 totalCount
	@Override
	public int searchToDayTotalCount(String userId) {
		log.info("searchToDayTotalCountByReservDateUserId()");
		return reservMapper.selectToDayTotalCount(userId);
	}

	// 이전 예약 목록 totalCount
	@Override
	public int searchPrevDayTotalCount(String userId) {
		log.info("searchPrevDayTotalCountByReservDateUserId()");
		return reservMapper.selectPrevDayTotalCount(userId);
	}
	
	// store 상세
	@Override
	public StoreVO searchStoreByStoreId(int storeId) {
		log.info("searchStoreByStoreId()");;
		return storeMapper.selectStoreById(storeId);
	}
	
	// 예약 등록
	@Override
	public int createdReserv(ReservVO reservVO) {
		log.info("insertReserv()");
		int result = reservMapper.insert(reservVO);
		return result;
	}

	// 예약 취소
	@Override
	public int cancelReserv(int reservId) {
		log.info("cancelReserv()");
		int result = reservMapper.delete(reservId);
		return result;
	}

	// 예약 가능시간 조회
	@Override
	public List<StoreScheduleVO> searchSchedule(StoreScheduleVO storeScheduleVO) {
		List<StoreScheduleVO> list = reservMapper.selectSchedule(storeScheduleVO);
		
		List<StoreScheduleVO> resultList = new ArrayList<>();
		int totalPersonnel = storeScheduleVO.getTotalPersonnel();
		int reservLimit = storeScheduleVO.getReservLimit();
		
		for(int i = 0; i < list.size(); i++) {
			if(totalPersonnel <= reservLimit) {
				resultList.add(list.get(i));
			}
		}
		
		return resultList;
	}
	
}
