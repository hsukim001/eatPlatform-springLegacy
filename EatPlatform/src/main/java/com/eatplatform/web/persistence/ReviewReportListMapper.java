package com.eatplatform.web.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.ReviewReportListVO;
import com.eatplatform.web.domain.ReviewReportListWithUserAtNameVO;
import com.eatplatform.web.util.Pagination;

@Mapper
public interface ReviewReportListMapper {
	
	/**
	 * @param Pagination pagination
	 * @return List<ReviewReportListWithUserAtName>
	 */
	List<ReviewReportListWithUserAtNameVO> selectReportListByPagination(Pagination pagination);
	
	/**
	 * @param reviewId
	 * @return
	 */
	List<ReviewReportListVO> selectReportListByReviewId(int reviewId);
	
	/**
	 * 신고 총 건 수 조회
	 * @return int
	 */
	int getTotalCount();
	
	int insert(ReviewReportListVO reviewReportListVO);
	
	// 신고여부 확인
	int checkReport(ReviewReportListVO reviewReportListVO);
	
	/**
	 * @param reviewId
	 * @return
	 */
	int deleteReviewReportByReviewId(int reviewId);
	
}
