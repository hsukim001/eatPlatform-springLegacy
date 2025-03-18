package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.JoinReviewReportVO;
import com.eatplatform.web.domain.ReviewReportListVO;
import com.eatplatform.web.service.ReviewReportListService;

@RestController
@RequestMapping("/management")
public class ManagementRESTController {
	
	@Autowired
	private ReviewReportListService reviewReportListService;
	
	@GetMapping("/report/review/{reviewId}")
	public ResponseEntity<Map<String, Object>> getReviewReportInfo(@PathVariable("reviewId") int reviewId) {
		Map<String, Object> map = new HashMap<>();
		
		JoinReviewReportVO reviewReportInfo = reviewReportListService.getReviewReportInfo(reviewId);
		List<ReviewReportListVO> reviewReportList = reviewReportListService.getReviewReportListByReviewId(reviewId);
		
		map.put("info", reviewReportInfo);
		map.put("list", reviewReportList);
		
		
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	
	@DeleteMapping("/report/review/delete/{reviewId}")
	public ResponseEntity<Integer> deleteReviewReport(@PathVariable("reviewId") int reviewId) {
		int result = reviewReportListService.deleteReview(reviewId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
}
