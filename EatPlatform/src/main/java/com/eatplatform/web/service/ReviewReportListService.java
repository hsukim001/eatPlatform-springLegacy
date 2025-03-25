package com.eatplatform.web.service;

import java.util.List;

import com.eatplatform.web.domain.JoinReviewReportVO;
import com.eatplatform.web.domain.ReviewReportListVO;
import com.eatplatform.web.util.Pagination;

public interface ReviewReportListService {
	
	/**
	 * 신고 목록 조회
	 * @param Pagination pagination
	 * @return List<ReviewReportListWithUserAtName>
	 */
	List<JoinReviewReportVO> getReviewReportList(Pagination pagination);
	
	/**
	 * @param reviewId
	 * @return
	 */
	List<ReviewReportListVO> getReviewReportListByReviewId(int reviewId);
	
	/**
	 * @param reviewId
	 * @return
	 */
	JoinReviewReportVO getReviewReportInfo(int reviewId);
	
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
	
	/**
	 * @param reviewId
	 * @param reviewReportList
	 * @return
	 */
	int deleteReview(int reviewId);
}
