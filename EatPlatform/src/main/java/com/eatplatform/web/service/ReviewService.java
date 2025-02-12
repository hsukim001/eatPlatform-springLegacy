package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReviewVO;

public interface ReviewService {
	int createReview(ReviewVO reviewVO);
	List<ReviewVO> getAllReview(int storeId);
	ReviewVO getReviewById(int reviewId); // reviewId 조회
	
	int updateReview(ReviewVO reviewVO);
	int deleteReview(int reviewId);
	
	// 페이징
	int countAllReviewsByStoreId(int storeId);
	List<ReviewVO> getAllReviewsByStoreId(int storeId, int start, int end);
	
	// username 조회
	List<ReviewVO> getReviewWithUsername(int storeId);

}
