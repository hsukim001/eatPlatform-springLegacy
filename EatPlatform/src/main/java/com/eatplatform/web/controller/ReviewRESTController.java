package com.eatplatform.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatplatform.web.domain.CustomUser;
import com.eatplatform.web.domain.ReviewImageVO;
import com.eatplatform.web.domain.ReviewReportListVO;
import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.service.ReviewImageService;
import com.eatplatform.web.service.ReviewLikeListService;
import com.eatplatform.web.service.ReviewReportListService;
import com.eatplatform.web.service.ReviewService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping(value = "/review")
@Log4j
public class ReviewRESTController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ReviewImageService reviewImageService;
	
	@Autowired
	private ReviewLikeListService reviewLikeListService;
	
	@Autowired
	private ReviewReportListService reviewReportListService;
	
	/**
	 * 리뷰 등록(회원)
	 * @param model
	 * @param reviewVO
	 * @param customUser
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Integer> createReview(Model model, @RequestBody ReviewVO reviewVO,
			@AuthenticationPrincipal CustomUser customUser) {

		int userId = customUser.getUser().getUserId();
		reviewVO.setUserId(userId);
		
		// 필수 필드 검증(공백 및 글자 수 제한)
		if (reviewVO.getReviewContent() == null || reviewVO.getReviewContent().trim().isEmpty()) {
			model.addAttribute("error", "Review content cannot be empty.");
			return new ResponseEntity<>(0, HttpStatus.FORBIDDEN);
		} else if(reviewVO.getReviewContent() != null && reviewVO.getReviewContent().length() > 250) {
			return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
		}

		log.info("createReview()");
		log.info("reviewVO : " + reviewVO);

		int result = reviewService.createReview(reviewVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	/**
	 * 식당별 리뷰 전체 조회
	 * @param storeId
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/all/{storeId}")
	public ResponseEntity<Map<String, Object>> readAllReview(@PathVariable("storeId") int storeId,
			@RequestParam("page") int pageNumber, @RequestParam("pageSize") int pageSize) {
		log.info("readAllReview()");
		log.info("storeId = " + storeId);

		int totalReviews = reviewService.countAllReviewsByStoreId(storeId);
		log.info("totalReviews = " + totalReviews);

		int start = (pageNumber - 1) * pageSize + 1;
		int end = pageNumber * pageSize;

		List<ReviewVO> list = reviewService.getPagingReviewsByStoreId(storeId, start, end);
		

		// 첨부된 이미지 데이터 조회 및 username 가져오기
		for (ReviewVO reviewVO : list) {
			int reviewId = reviewVO.getReviewId();
			List<ReviewImageVO> reviewImageList = reviewImageService.getImageListByReviewId(reviewId);
			reviewVO.setReviewImageList(reviewImageList);
			ReviewVO username = reviewService.getReviewWithUsername(reviewId);
			reviewVO.setUserVO(username.getUserVO());
		}
		
		log.info("list : " + list);
		log.info("start : " + start);
		log.info("end : " + end);

		Map<String, Object> result = new HashMap<>();
		result.put("list", list);
		result.put("totalReviews", totalReviews); // 전체 리뷰 개수 포함
		result.put("pageNumber", pageNumber); // 현재 페이지 번호 포함
		result.put("pageSize", pageSize); // 한 페이지 당 리뷰 수 포함
		result.put("end", end);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 리뷰 삭제
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Integer> deleteReview(@PathVariable("reviewId") int reviewId) {

		log.info("deleteReview()");

		int result = reviewService.deleteReview(reviewId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);

	}

	// 리뷰아이디별 조회
	@GetMapping("/{reviewId}")
	public ResponseEntity<ReviewVO> getReviewWithUsername(@PathVariable("reviewId") int reviewId) {
		log.info("getReviewWithUsername()");

		ReviewVO reviewVO = reviewService.getReviewWithUsername(reviewId);
		log.info("reviewVO : " + reviewVO);

		return new ResponseEntity<ReviewVO>(reviewVO, HttpStatus.OK);

	}
	
	/**
	 * 리뷰 추천
	 * @param reviewId
	 * @param customUser
	 * @return
	 */
	@PostMapping("/like/{reviewId}")
	public ResponseEntity<Integer> createReviewLikeList(@PathVariable("reviewId") int reviewId,
			@AuthenticationPrincipal CustomUser customUser) {
		
		int userId = customUser.getUser().getUserId();
		
		log.info("createReviewLikeList()");
			
		int result = reviewLikeListService.createReviewLikeList(reviewId, userId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 리뷰 신고
	 * @param reviewReportListVO
	 * @param customUser
	 * @return
	 */
	@PostMapping("/report/{reviewId}")
	public ResponseEntity<Integer> createReviewReportList(@RequestBody ReviewReportListVO reviewReportListVO,
			@AuthenticationPrincipal CustomUser customUser) {
		
		int userId = customUser.getUser().getUserId();
		
		log.info("createReviewReportList()");
			
		reviewReportListVO.setUserId(userId);
		int result = reviewReportListService.createReviewReportList(reviewReportListVO);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
		
	}
	
	/**
	 * 신고여부 확인
	 * @param reviewId
	 * @param customUser
	 * @return
	 */
	@GetMapping("report/{reviewId}")
	public ResponseEntity<Integer> reviewReported(@PathVariable("reviewId") int reviewId,
			@AuthenticationPrincipal CustomUser customUser) {
		
		int userId = customUser.getUser().getUserId();
	
		log.info("reviewReported()");
			
		int result = reviewReportListService.isReviewReported(reviewId, userId);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
		
	}

}
