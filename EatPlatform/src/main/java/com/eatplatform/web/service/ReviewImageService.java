package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReviewImageVO;

public interface ReviewImageService {
	
	ReviewImageVO getReviewImageById(int reviewImageId);
	List<ReviewImageVO> getImageListByReviewId(int reviewId);
	
}
