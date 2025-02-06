package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.service.ReviewLikeListService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/review/like")
@Log4j
public class ReviewLikeListRESTController {
	
	@Autowired
	private ReviewLikeListService reviewLikeListService;
	
	@PostMapping("/{reviewId}")
	public ResponseEntity<Integer> createReviewLikeList(
			@PathVariable("reviewId") int reviewId,
			@AuthenticationPrincipal UserDetails userDetails) {
		
		log.info("createReviewLikeList()");
		
		String userId = userDetails.getUsername();
		int result = reviewLikeListService.createReviewLikeList(reviewId, userId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

}
