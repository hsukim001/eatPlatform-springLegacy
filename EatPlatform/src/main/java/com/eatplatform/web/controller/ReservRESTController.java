package com.eatplatform.web.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.ReservVO;
import com.eatplatform.web.domain.StoreScheduleVO;
import com.eatplatform.web.service.ReservService;
import com.eatplatform.web.util.DataResponse;
import com.eatplatform.web.util.PageMaker;
import com.eatplatform.web.util.Pagination;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/reserv")
@Log4j
public class ReservRESTController {

	@Autowired
	private ReservService reservService;

	// 페이징 예약 목록 조회(pageNum, userId)
	@GetMapping("/toDay/{pageNum}")
	public ResponseEntity<DataResponse> searchPagingToDay(@PathVariable("pageNum") int pageNum, @AuthenticationPrincipal CustomUser customUser) {
		log.info("searchPagingToDay()");
		int pageSize = 5;
		log.info("pageNum = " + pageNum);
		
		// session에서 사용자 아이디 로드
		int userId = customUser.getUser().getUserId();
		

		Pagination pagination = new Pagination(userId, pageNum, pageSize);

		List<ReservVO> list = reservService.searchToDayList(pagination);

		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		int totalCount = reservService.searchToDayTotalCount(userId);
		log.info("totalCount : " + totalCount);
		pageMaker.setTotalCount(totalCount);

		DataResponse dataResponse = new DataResponse(list, pageMaker);

		return new ResponseEntity<DataResponse>(dataResponse, HttpStatus.OK);
	}

	// 페이징 이전 예약 목록 조회(pageNum, userId)
	@GetMapping("/prevDay/{pageNum}")
	public ResponseEntity<DataResponse> searchPagingPrevDay(@PathVariable("pageNum") int pageNum,
															@AuthenticationPrincipal CustomUser customUser) {
		log.info("searchPagingToDay()");
		int pageSize = 5;
		
		// session에서 사용자 아이디 로드
		int userId = customUser.getUser().getUserId();

		Pagination pagination = new Pagination(userId, pageNum, pageSize);

		List<ReservVO> list = reservService.searchPrevDayList(pagination);

		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		int totalCount = reservService.searchPrevDayTotalCount(userId);
		pageMaker.setTotalCount(totalCount);
		log.info("prev totalCount : " + totalCount);

		DataResponse dataResponse = new DataResponse(list, pageMaker);

		return new ResponseEntity<DataResponse>(dataResponse, HttpStatus.OK);
	}

	// 예약 등록
	@PostMapping("/created/{reservLimit}")
	public ResponseEntity<Integer> createdReserv(@RequestBody ReservVO reservVO, @AuthenticationPrincipal CustomUser customUser, @PathVariable("reservLimit") int reservLimit) {
		log.info("createdReserv()");
		ReservVO vo = reservVO;
		
		int userId = customUser.getUser().getUserId();
		vo.setUserId(userId);
		int result = reservService.createdReserv(vo, reservLimit);
		
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// 예약 취소
	@DeleteMapping("/cancel/{reservId}")
	public ResponseEntity<Integer> cancelReserv(@PathVariable("reservId") int reservId) {
		log.info("cancelReserv()");
		int result = reservService.cancelReserv(reservId);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 예약 가능시간 조회
	@GetMapping("/schedule/{storeId}/{reservLimit}/{personnel}/{reservDate}")
	public ResponseEntity<List<StoreScheduleVO>> searchSchedule(@PathVariable("storeId") int storeId, @PathVariable("reservLimit") int reservLimit, 
			@PathVariable("personnel") int personnel, @PathVariable("reservDate") String reservDate) {
		log.info("searchSchedule()");
		
		StoreScheduleVO vo = new StoreScheduleVO();
		vo.setStoreId(storeId);
		vo.setReservLimit(reservLimit);
		vo.setReservDate(reservDate);
		List<StoreScheduleVO> list = reservService.searchSchedule(vo, personnel);
		for(int i = 0; i < list.size(); i++) {
			log.info(list.get(i).getReservDate());
		}
		
		return new ResponseEntity<List<StoreScheduleVO>>(list, HttpStatus.OK);
	}

}
