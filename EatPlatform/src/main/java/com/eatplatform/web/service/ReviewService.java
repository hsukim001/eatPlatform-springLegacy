package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReviewVO;

public interface ReviewService {
	int createReview(ReviewVO reviewVO);
	List<ReviewVO> getAllReview(int storeId);
	ReviewVO getReviewById(int reviewId); // reviewId 조회
	
	int updateReview(ReviewVO reviewVO);
	int deleteReview(int reviewId);
	 
	/**
	 * 페이징
	 * @param storeId
	 * @param start
	 * @param end
	 * @return
	 */
	List<ReviewVO> getPagingReviewsByStoreId(int storeId, int start, int end);
	int countAllReviewsByStoreId(int storeId);
	
	// username 조회
	ReviewVO getReviewWithUsername(int reviewId);

}
