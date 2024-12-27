package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.service.ReplyService;
import com.eatplatform.web.service.ReviewService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/review")
@Log4j
public class ReviewRESTController {
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReplyService replyService;
	
	// 리뷰 등록(회원)
	@PostMapping
	public ResponseEntity<Integer> createReview(
			@RequestBody ReviewVO reviewVO, HttpSession session) {
		
		// 세션에서 userId 가져오기
		String userId = (String) session.getAttribute("userId");
		
		// 리뷰 내용 길이 제한 (250자 이하) 
		if (reviewVO.getReviewContent() != null && 
				reviewVO.getReviewContent().length() > 250) {
			return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
		}
		
		log.info("createReview()");
		
		int result = reviewService.createReview(reviewVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 식당별 리뷰 전체 조회
		@GetMapping("/all/{storeId}")
		public ResponseEntity<Map<String, Object>> readAllReview(
				@PathVariable("storeId") int storeId,
				@RequestParam("page") int pageNumber,
				@RequestParam("pageSize") int pageSize) {
			log.info("readAllReview()");
			log.info("storeId = " + storeId);
			
			int totalReviews = reviewService.countAllReviewsByStoreId(storeId);
			log.info("totalReviews = " + totalReviews);
			
			int start = (pageNumber - 1) * pageSize + 1;
			int end = pageNumber * pageSize;
			
			List<ReviewVO> list = reviewService.getAllReviewsByStoreId(storeId, start, end);
			log.info("list : " + list);
			log.info("start : " + start);
			log.info("end : " + end);
			
			Map<String, Object> result = new HashMap<>();
		    result.put("list", list);
		    result.put("totalReviews", totalReviews);  // 전체 리뷰 개수 포함
		    result.put("pageNumber", pageNumber);     // 현재 페이지 번호 포함
		    result.put("pageSize", pageSize);         // 한 페이지 당 리뷰 수 포함
		    result.put("end", end);
		    return new ResponseEntity<>(result, HttpStatus.OK);
		}
	
	// 리뷰 수정
	@PutMapping("/{reviewId}")
	public ResponseEntity<Integer> updateReview(
			@PathVariable int reviewId, 
			@RequestBody ReviewVO reviewVO) {
		log.info("updateReview()");
		
		
			
		int result = reviewService.updateReview(reviewVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// 리뷰 삭제
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Integer> deleteReview(
			@PathVariable("reviewId") int reviewId) {
		log.info("deleteReview()");
		
		int result = reviewService.deleteReview(reviewId);
		int deleteReply = replyService.deleteReplyByReview(reviewId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	

	
	
}
