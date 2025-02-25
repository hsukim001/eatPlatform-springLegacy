package com.eatplatform.web.service;

public interface ReviewLikeListService {
	/**
	 * 리뷰 추천 리스트 등록
	 * @param reviewId
	 * @param userId
	 * @return
	 */
	int createReviewLikeList(int reviewId, int userId);
}
