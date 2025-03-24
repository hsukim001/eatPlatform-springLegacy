package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.service.ReviewService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/review")
@Log4j
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	/**
	 * 선택된 리뷰를 updateReview.jsp로 전송
	 * @param model
	 * @param reviewId
	 */
	@GetMapping("/updateReview")
	public void updateReviewGET(Model model, int reviewId) {
		log.info("updateReviewGET()");
		ReviewVO reviewVO = reviewService.getReviewById(reviewId);
		model.addAttribute("reviewVO",reviewVO);
		log.info("reviewVO : " + reviewVO);
		
	}
	 
	/**
	 * updateReview.jsp에서 수정할 데이터를 전송받아 게시글 데이터 수정
	 * @param model
	 * @param reviewVO
	 * @return
	 */
	@PostMapping("/updateReview")
	public String updateReviewPOST(Model model, ReviewVO reviewVO) {
		log.info("updateReviewPOST()");
		
		// 필수 필드 검증
		if (reviewVO.getReviewContent() == null || reviewVO.getReviewContent().trim().isEmpty()) {
			model.addAttribute("error", "Review content cannot be empty.");
			return "redirect:/review/updateReview?reviewId=" + reviewVO.getReviewId();  // 오류가 있을 경우 수정 페이지로 돌아가기
		}
		
		int result = reviewService.updateReview(reviewVO);
		log.info("reviewVO = " + reviewVO.toString());
		log.info(result + "행 수정");
		
		return "redirect:/store/detail?storeId=" + reviewVO.getStoreId();
		
	}

}
