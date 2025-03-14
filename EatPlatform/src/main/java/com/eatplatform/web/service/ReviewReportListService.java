package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.ReviewReportListVO;
import com.eatplatform.web.domain.ReviewReportListWithUserAtNameVO;
import com.eatplatform.web.util.Pagination;

public interface ReviewReportListService {
	
	/**
	 * 신고 목록 조회
	 * @param Pagination pagination
	 * @return List<ReviewReportListWithUserAtName>
	 */
	List<ReviewReportListWithUserAtNameVO> getReviewReportList(Pagination pagination);
	
	/**
	 * 신고 총 건수 조회
	 * @return int
	 */
	int getTotalCount();
	
	int createReviewReportList(ReviewReportListVO reviewReportListVO);
	 
	/**
	 * 신고여부 확인
	 * @param reviewId
	 * @param userId
	 * @return
	 */
	int isReviewReported(int reviewId, int userId);
}
