package com.eatplatform.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.service.ReviewService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value = "/page")
@Log4j
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/detail")
	public void detail() {
		log.info("detail()");
	}
	
	@GetMapping("/updateReview")
	public void updateReview(Model model, Integer reviewId) {
		log.info("updateReview()");
		log.info("reviewId : " + reviewId);
		ReviewVO reviewVO = reviewService.getReviewById(reviewId);
		model.addAttribute("reviewVO",reviewVO);
		log.info("reviewVO : " + reviewVO);
	}

}
