package com.eatplatform.web.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
	@Transactional(value = "transactionManager", isolation = Isolation.SERIALIZABLE)
	@Override
	public int createdReserv(ReservVO reservVO, int reservLimit) {
		log.info("insertReserv()");
		int maxRetries = 3;
		int attempts = 0;
		
		while (attempts < maxRetries) {
			try {
//				return insertTransaction(reservVO, reservLimit);
				reservMapper.lockReservTable();
				int currentReservPersonnel = 0;
				int result = 0;
				
				int dummy = reservMapper.insertDummyIfNotMatched(reservVO);
				log.info("Dummy result : " + dummy);
				
				List<ReservVO> list = reservMapper.selectScheduleForUpdate(reservVO, reservLimit);
				if(!ObjectUtils.isEmpty(list)) {
					log.info("list is not null");
					for(int i = 0; i < list.size(); i++) {
						currentReservPersonnel += list.get(i).getReservPersonnel();
					}
				} else {
					log.info("데이터 없음");
				}
				
				int totalPersonnel = currentReservPersonnel + reservVO.getReservPersonnel();
				log.info("인원 : " + totalPersonnel);
				
				if(totalPersonnel <= reservLimit) {
					if(dummy == 0) {
						result = reservMapper.insert(reservVO);		
					} else {
						int reservId = reservMapper.selectSequenceCurrval();
						reservVO.setReservId(reservId);
						result = reservMapper.updateDummy(reservVO);
					}
					log.info("예약 등록 성공");
				} else {
					log.info("예약 실패 : 인원 초과");
				}
				
				return result;
			} catch (Exception e) {
				attempts++;
				log.info("동시성 충돌 재시도");
				
				if(attempts >= maxRetries) {
//					throw e;
					log.error("최대 재시도 횟수 초과");
				}
			}
		}
		
		return 0;
	}
	
	// 트랜잭션을 활용한 등록
	@Transactional(value = "transactionManager", isolation = Isolation.SERIALIZABLE)
	private int insertTransaction(ReservVO reservVO, int reservLimit) {
		log.info("insertTransaction()");
		int dummy = reservMapper.insertDummyIfNotMatched(reservVO);
		log.info("Dummy result : " + dummy);
		
		List<ReservVO> list = reservMapper.selectScheduleForUpdate(reservVO, reservLimit);
		int currentReservPersonnel = 0;
		int result = 0;
		
		if(!ObjectUtils.isEmpty(list)) {
			log.info("list is not null");
			for(int i = 0; i < list.size(); i++) {
				currentReservPersonnel += list.get(i).getReservPersonnel();
			}
		} else {
			log.info("데이터 없음");
		}
		
		int totalPersonnel = currentReservPersonnel + reservVO.getReservPersonnel();
		log.info("인원 : " + totalPersonnel);
		if(totalPersonnel <= reservLimit) {
			if(dummy == 0) {
				result = reservMapper.insert(reservVO);		
			} else {
				int reservId = reservMapper.selectSequenceCurrval();
				reservVO.setReservId(reservId);
				result = reservMapper.updateDummy(reservVO);
			}
			log.info("예약 등록 성공");
		} else {
			log.info("예약 실패 : 인원 초과");
		}
		
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
	public List<StoreScheduleVO> searchSchedule(StoreScheduleVO storeScheduleVO, int personnel) {
		log.info("searchSchedule()");
		log.info(storeScheduleVO.getReservDate());
		
		List<StoreScheduleVO> list = reservMapper.selectSchedule(storeScheduleVO);
		int reservLimit = storeScheduleVO.getReservLimit();
		
		for(int i = 0; i < list.size(); i++) {
			int totalPersonnel = list.get(i).getTotalPersonnel();
			log.info(list.get(i).getReservDate());
			
			if(totalPersonnel < reservLimit) {
				int maxPeople = reservLimit - totalPersonnel;
				
				if(personnel <= maxPeople) {
					list.get(i).setActive(true);
				}
			}
		}
		
		return list;
	}
	
}
