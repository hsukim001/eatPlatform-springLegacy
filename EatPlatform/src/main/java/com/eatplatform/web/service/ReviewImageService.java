package com.eatplatform.web.service;


import com.eatplatform.web.domain.ReviewImageVO;

public interface ReviewImageService {

	int reviewImage(ReviewImageVO reviewImageVO);
	ReviewImageVO getReviewImageById(int reviewImageId);
	
}
