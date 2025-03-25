package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.StoreApprovalsVO;
import com.eatplatform.web.service.StoreApprovalsService;

import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping(value = "/approval")
public class StoreApprovalsRESTController {
	
	@Autowired
	private StoreApprovalsService storeApprovalsService;
	
	// 가게 등록 요청 승인
	@PutMapping("/store")
	public ResponseEntity<Integer> storeApproval(@RequestBody StoreApprovalsVO storeApprovalsVO) {
		log.info("storeApproval()");
		int result = storeApprovalsService.storeApproval(storeApprovalsVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 가게 등록 요청 거부, 취소
	@PutMapping("/denialManagement/{storeId}")
	public ResponseEntity<Integer> denialManagement(@PathVariable("storeId") int storeId) {
		log.info("denialManagement()");
		int result = storeApprovalsService.denialManagement(storeId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
