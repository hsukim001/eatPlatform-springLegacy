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
public class ReservServiceImple implements ReservService {

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
	public int searchToDayTotalCount(int userId) {
		log.info("searchToDayTotalCountByReservDateUserId()");
		return reservMapper.selectToDayTotalCount(userId);
	}

	// 이전 예약 목록 totalCount
	@Override
	public int searchPrevDayTotalCount(int userId) {
		log.info("searchPrevDayTotalCountByReservDateUserId()");
		return reservMapper.selectPrevDayTotalCount(userId);
	}

	// 예약 등록
	@Override
	public int createdReserv(ReservVO reservVO, int reservLimit) {
		log.info("insertReserv()");
		
		int currentReservPersonnel = 0;
		int result = 0;
		
		try {
			
			Date nowDate = new Date();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String nowDateFormat = formatter.format(nowDate);
			String dateTime = reservVO.getReservDate() + " " + reservVO.getReservHour() + ":" + reservVO.getReservMin();
			
			Date now = formatter.parse(nowDateFormat);
			Date reservDateTime = formatter.parse(dateTime);
			
			// 현재시간보다 이전의 시간 예외
			if (now.after(reservDateTime)) {
				log.info("예약할수 없는 시간입니다.");
				return result;
			}
			
			currentReservPersonnel = reservMapper.selectTotalPersonnelByStoreIdDateHourMin(reservVO);
			log.info("currentReservPersonnel : " + currentReservPersonnel);
			
			int totalPersonnel = currentReservPersonnel + reservVO.getReservPersonnel();
			log.info("인원 : " + totalPersonnel);
			
			if (totalPersonnel <= reservLimit) {
				result = reservMapper.insert(reservVO);
				log.info("예약 등록 성공");
			} else {
				log.info("예약 실패 : 인원 초과");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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

		for (int i = 0; i < list.size(); i++) {
			int totalPersonnel = list.get(i).getTotalPersonnel();
			log.info(list.get(i).getReservDate());

			if (totalPersonnel < reservLimit) {
				int maxPeople = reservLimit - totalPersonnel;

				if (personnel <= maxPeople) {
					list.get(i).setActive(true);
				}
			}
		}

		return list;
	}

}
