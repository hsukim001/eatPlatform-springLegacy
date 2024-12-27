package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReviewImageVO;

public interface ReviewImageService {
	int createReviewImage(ReviewImageVO reviewImageVO);
	ReviewImageVO getReviewImageById(int reviewImageId);
	List<ReviewImageVO> getAllImageId();
	int updateReviewImage(ReviewImageVO reviewImageVO);
	int deleteReviewImage(int reviewImageId);
}
