package com.eatplatform.web.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.eatplatform.web.service.ReservService;

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
	
	// 예약 목록 조회 (userId)
	@GetMapping("/toDay/{userId}")
	public ResponseEntity<List<ReservVO>> searchToDayByReservDateUserId(@PathVariable("userId") String userId) {
		log.info("searchToDayByReservDateUserId()");
		log.info(userId);
		List<ReservVO> list = new ArrayList<ReservVO>();
		list = reservService.searchToDayByReservDateUserId(userId);
		return new ResponseEntity<List<ReservVO>>(list, HttpStatus.OK);
	}
	
	// 이전 예약 목록 조회(userId)
	@GetMapping("/prevDay/{userId}")
	public ResponseEntity<List<ReservVO>> searchPrevDayByReservDateUserId(@PathVariable("userId") String userId) {
		log.info("searchPrevDayByReservDateUserId()");
		log.info(userId);
		List<ReservVO> list = new ArrayList<ReservVO>();
		list = reservService.searchPrevDayByReservDateUserId(userId);
		return new ResponseEntity<List<ReservVO>>(list, HttpStatus.OK);
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
