package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.ReservVO;
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

	// 예약 목록 검색(사용자 아이디)
	@GetMapping("/myReserv/{userId}")
	public ResponseEntity<List<ReservVO>> searchByUserId(@PathVariable("userId") String userId) {
		log.info("searchByUserId()");
		List<ReservVO> list = reservService.searchByUserId(userId);

		return new ResponseEntity<List<ReservVO>>(list, HttpStatus.OK);
	}

	// 예약 상세 조회
	@GetMapping("/search/{reservId}")
	public ResponseEntity<ReservVO> searchByReservId(@PathVariable("reservId") int reservId) {
		log.info("searchByReservId()");
		ReservVO vo = reservService.searchByReservId(reservId);

		return new ResponseEntity<ReservVO>(vo, HttpStatus.OK);
	}

	// 페이징 예약 목록 조회(pageNum, userId)
	@GetMapping("/toDay/{userId}")
	public ResponseEntity<DataResponse> searchPagingToDay(@PathVariable("userId") String userId,
			@RequestParam(defaultValue = "1") int pageNum) {
		log.info("searchPagingToDay()");
		int pageSize = 5;

		Pagination pagination = new Pagination(userId, pageNum, pageSize);

		List<ReservVO> list = reservService.searchPagingToDay(pagination);

		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		int pageCount = reservService.searchToDayTotalCountByReservDateUserId(userId);
		log.info("pageCount : " + pageCount);
		pageMaker.setPageCount(pageCount);

		DataResponse dataResponse = new DataResponse(list, pageMaker);

		return new ResponseEntity<DataResponse>(dataResponse, HttpStatus.OK);
	}

	// 페이징 이전 예약 목록 조회(pageNum, userId)
	@GetMapping("/prevDay/{userId}")
	public ResponseEntity<DataResponse> searchPagingPrevDay(@PathVariable("userId") String userId,
			@RequestParam(defaultValue = "1") int pageNum) {
		log.info("searchPagingToDay()");
		int pageSize = 5;

		Pagination pagination = new Pagination(userId, pageNum, pageSize);

		List<ReservVO> list = reservService.searchPagingPrevDay(pagination);

		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		int pageCount = reservService.searchToDayTotalCountByReservDateUserId(userId);
		log.info("pageCount : " + pageCount);
		pageMaker.setPageCount(pageCount);

		DataResponse dataResponse = new DataResponse(list, pageMaker);

		return new ResponseEntity<DataResponse>(dataResponse, HttpStatus.OK);
	}

	@GetMapping("/toDay/list")
	public Map<String, Object> ajaxToDayList(@RequestParam("userId") String userId, @RequestParam(value="pageNum", defaultValue = "1") int pageNum) {
		
		log.info("ajaxToDayList()");
		int pageSize = 5;

		Pagination pagination = new Pagination(userId, pageNum, pageSize);

		List<ReservVO> list = reservService.searchPagingToDay(pagination);

		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		int pageCount = reservService.searchToDayTotalCountByReservDateUserId(userId);
		log.info("pageCount : " + pageCount);
		pageMaker.setPageCount(pageCount);
		
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("pageMaker", pageMaker);
		
		return map;
	}

	// 예약 등록
	@PostMapping
	public ResponseEntity<Integer> createdReserv(@RequestBody ReservVO reservVO) {
		log.info("createdReserv()");
		int result = reservService.createdReserv(reservVO);
		
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// 예약 삭제
	@DeleteMapping("/{reservId}")
	public ResponseEntity<Integer> deleteReserv(@PathVariable("reservId") int reservId) {
		log.info("deleteReserv()");
		int result = reservService.deleteReserv(reservId);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

}
