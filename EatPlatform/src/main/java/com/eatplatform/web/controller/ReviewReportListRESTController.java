package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.ReviewReportListVO;
import com.eatplatform.web.service.ReviewReportListService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/review/report")
@Log4j
public class ReviewReportListRESTController {
	
	@Autowired
	private ReviewReportListService reviewReportListService;
	
	@PostMapping("/{reviewId}")
	public ResponseEntity<Integer> createReviewReportList(
			@RequestBody ReviewReportListVO reviewReportListVO) {
		log.info("createReviewReportList()");
		int result = reviewReportListService.createReviewReportList(reviewReportListVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
