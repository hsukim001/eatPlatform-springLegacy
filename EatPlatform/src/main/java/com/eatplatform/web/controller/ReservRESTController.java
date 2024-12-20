package com.eatplatform.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<DataResponse> searchPagingToDay(@PathVariable("pageNum") int pageNum, HttpServletRequest request) {
		log.info("searchPagingToDay()");
		int pageSize = 5;
		log.info("pageNum = " + pageNum);
		
		// session에서 사용자 아이디 로드
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		

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
			HttpServletRequest request) {
		log.info("searchPagingToDay()");
		int pageSize = 5;
		
		// session에서 사용자 아이디 로드
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");

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
	@PostMapping("/created")
	public ResponseEntity<Integer> createdReserv(@RequestBody ReservVO reservVO) {
		log.info("createdReserv()");
		int result = reservService.createdReserv(reservVO);
		
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
	@GetMapping("/schedule/{storeId}/{reservDate}/{reservLimit}")
	public ResponseEntity<List<StoreScheduleVO>> searchSchedule(@PathVariable("storeId") int storeId, 
			@PathVariable("reservDate") String reservDate,
			@PathVariable("reservLimit") int reservLimit) {
		StoreScheduleVO vo = new StoreScheduleVO(storeId, reservDate, reservLimit);
		List<StoreScheduleVO> list = reservService.searchSchedule(vo);
		
		return new ResponseEntity<List<StoreScheduleVO>>(list, HttpStatus.OK);
	}

}
