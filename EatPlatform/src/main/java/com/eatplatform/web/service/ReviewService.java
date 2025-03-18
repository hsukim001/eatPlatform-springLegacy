package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReviewVO;

public interface ReviewService {
	
	List<ReviewVO> getAllReview(int storeId);
	
	/**
	 * 페이징
	 * @param storeId
	 * @param start
	 * @param end
	 * @return
	 */
	List<ReviewVO> getPagingReviewsByStoreId(int storeId, int start, int end);
	ReviewVO getReviewById(int reviewId); // reviewId 조회
	ReviewVO getReviewWithUsername(int reviewId);
	
	int createReview(ReviewVO reviewVO);
	int updateReview(ReviewVO reviewVO);
	int deleteReview(int reviewId);
	 
	int countAllReviewsByStoreId(int storeId);
	
	/**
	 * 신고된 리뷰 총 건수 조회
	 * @return int
	 */
	int getReviewReportCount();
	
}
