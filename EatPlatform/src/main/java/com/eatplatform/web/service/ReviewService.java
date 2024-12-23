package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReviewVO;
import com.eatplatform.web.util.Pagination;

public interface ReviewService {
	int createReview(ReviewVO reviewVO);
	List<ReviewVO> getAllReview(int storeId);
	int updateReview(ReviewVO reviewVO);
	int deleteReview(int reviewId);
	
	List<ReviewVO> getPagingReviews(Pagination pagination);
	int getReviewCount();
	
}
