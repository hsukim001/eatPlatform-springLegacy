package com.eatplatform.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.service.ReviewService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/review")
@Log4j
public class ReviewRESTController {
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping
	public ResponseEntity<Integer> createReview(
			@RequestBody ReviewVO reviewVO) {
		log.info("createReview()");
		
		int result = reviewService.createReview(reviewVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	@GetMapping("/all/{storeId}")
	public ResponseEntity<List<ReviewVO>> readAllReview(
			@PathVariable("storeId") int storeId) {
		log.info("readAllReview()");
		log.info("storeId = " + storeId);
		
		List<ReviewVO> list = reviewService.getAllReview(storeId);
		return new ResponseEntity<List<ReviewVO>>(list, HttpStatus.OK);
	}
	
	@PutMapping("/{reviewId}")
	public ResponseEntity<Integer> updateReview(
			@PathVariable int reviewId, 
			@RequestBody ReviewVO reviewVO) {
		log.info("updateReview()");
		
		int result = reviewService.updateReview(reviewVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Integer> deleteReview(
			@PathVariable("reviewId") int reivewId) {
		log.info("deleteReview()");
		
		int result = reviewService.deleteReview(reivewId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
