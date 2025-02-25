package com.eatplatform.web.service;

import com.eatplatform.web.domain.ReviewReportListVO;

public interface ReviewReportListService {
	int createReviewReportList(ReviewReportListVO reviewReportListVO);
	 
	/**
	 * 신고여부 확인
	 * @param reviewId
	 * @param userId
	 * @return
	 */
	int isReviewReported(int reviewId, int userId);
}
